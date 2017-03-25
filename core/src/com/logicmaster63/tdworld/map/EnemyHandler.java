package com.logicmaster63.tdworld.map;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.logicmaster63.tdworld.enemy.Enemy;

import java.util.ArrayList;

public class EnemyHandler {

    private ArrayList<Enemy> enemies;
    private Vector3 pos;

    public EnemyHandler(Vector3 pos, ArrayList<Enemy> enemies) {
        this.enemies = enemies;
        this.pos = pos;
    }

    public EnemyHandler(Vector3 pos) {
        this(pos, new ArrayList<Enemy>());
    }

    public void tick(float delta) {


        for(Enemy enemy: enemies)
            enemy.tick(delta);
    }

    public void render(float delta, ModelBatch batch) {
        for(Enemy enemy: enemies)
            enemy.render(delta, batch);
    }

    public Enemy add(Enemy enemy) {
        enemies.add(enemy);
        return enemy;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}
