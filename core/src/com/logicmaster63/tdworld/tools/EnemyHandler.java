package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.map.Spawn;
import com.logicmaster63.tdworld.object.Object;
import com.logicmaster63.tdworld.screens.GameScreen;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnemyHandler {

    private List<Enemy> enemies;
    private Vector3 pos;
    private boolean started = false;
    private double prevTime;
    private int enemyIndex;
    private Map<String, Class<?>> enemyClasses;
    private Map<String, Model> models;
    private List<Spawn> spawns;
    private List<Vector3> path;
    private btCollisionWorld world;
    private Map<Integer, Object> objects;

    public EnemyHandler(Vector3 pos, Map<String, Class<?>> enemyClasses, List<Spawn> spawns, Map<String, Model> models, List<Enemy> enemies, List<Vector3> path, btCollisionWorld world, Map<Integer, Object> objects) {
        this.enemies = enemies;
        this.pos = pos;
        this.enemyClasses = enemyClasses;
        this.models = models;
        this.spawns = spawns;
        this.path = path;
        this.world = world;
        this.objects = objects;
        System.out.println(spawns);
    }

    public EnemyHandler(Vector3 pos, Map<String, Class<?>> enemyClasses, List<Spawn> spawns, Map<String, Model> models, List<Vector3> path, btCollisionWorld world, Map<Integer, Object> objects) {
        this(pos, enemyClasses, spawns, models, new ArrayList<Enemy>(), path, world, objects);
    }

    public void tick(float delta, GameScreen screen) {
        if (!started && screen.isRunning()) {
            started = true;
            prevTime = System.currentTimeMillis();
        }
        if (spawns != null && enemyIndex < spawns.size() && System.currentTimeMillis() > prevTime + spawns.get(enemyIndex).getDelay()) {
            prevTime = System.currentTimeMillis();
            String name = spawns.get(enemyIndex).getName();
            ModelInstance instance = new ModelInstance(models.get(name));
            try {
                Class<?> c = enemyClasses.get(name);
                Constructor constructor = c.getConstructor(Vector3.class, ModelInstance.class, btCollisionWorld.class, Map.class, boolean.class);
                enemies.add((Enemy) constructor.newInstance(pos, instance, world, objects, false));
            } catch (Exception e) {
                Gdx.app.log("Error", e.toString());
            }
            enemyIndex++;
        }
        for (Enemy enemy : enemies)
            enemy.tick(delta, path);
    }

    public void render(float delta, ModelBatch batch) {
        for (Enemy enemy : enemies)
            enemy.render(delta, batch);
    }

    public Enemy add(Enemy enemy) {
        enemies.add(enemy);
        return enemy;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}
