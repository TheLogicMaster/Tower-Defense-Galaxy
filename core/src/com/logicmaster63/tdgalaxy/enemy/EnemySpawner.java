package com.logicmaster63.tdgalaxy.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.map.Spawn;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.screens.GameScreen;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnemySpawner {

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
    private IntMap<Entity> entity;

    public EnemySpawner(Vector3 pos, Map<String, Class<?>> enemyClasses, List<Spawn> spawns, Map<String, Model> models, List<Enemy> enemies, List<Vector3> path, btCollisionWorld world, IntMap<Entity> entities) {
        this.enemies = enemies;
        this.pos = pos;
        this.enemyClasses = enemyClasses;
        this.models = models;
        this.spawns = spawns;
        this.path = path;
        this.world = world;
        this.entity = entities;
        System.out.println(spawns);
    }

    public EnemySpawner(Vector3 pos, Map<String, Class<?>> enemyClasses, List<Spawn> spawns, Map<String, Model> models, List<Vector3> path, btCollisionWorld world, IntMap<Entity> entities) {
        this(pos, enemyClasses, spawns, models, new ArrayList<Enemy>(), path, world, entities);
    }

    public void tick(float delta, GameScreen screen) {
        if (!started && screen.isRunning()) {
            started = true;
            prevTime = System.currentTimeMillis();
        }
        if (spawns != null && enemyIndex < spawns.size() && System.currentTimeMillis() > prevTime + spawns.get(enemyIndex).getDelay()) {
            prevTime = System.currentTimeMillis();
            String name = spawns.get(enemyIndex).getName();
            ModelInstance instance;
            try {
                Class<?> c = enemyClasses.get(name);
                Constructor constructor = c.getConstructor(Matrix4.class, Map.class, btCollisionWorld.class, IntMap.class, List.class);
                enemies.add((Enemy) constructor.newInstance(new Matrix4().setToTranslation(pos), models, world, entity, path));
            } catch (Exception e) {
                Gdx.app.log("Error", e.toString());
            }
            enemyIndex++;
        }
        //for (Enemy enemy : enemies)
        //   enemy.tick(delta, path);
    }

    public Enemy add(Enemy enemy) {
        enemies.add(enemy);
        return enemy;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}
