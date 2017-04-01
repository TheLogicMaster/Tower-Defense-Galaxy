package com.logicmaster63.tdworld.enemy;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.screens.GameScreen;
import com.logicmaster63.tdworld.tools.Object;

import java.util.ArrayList;

public abstract class Enemy extends Object {

    private double speeed;
    private int spawnIndex = 0;

    public Enemy(Vector3 pos, double speeed, int hp, int health, int coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, ArrayList<Integer> ids) {
        super(pos, hp, health, coolDown, types, effects, instance, shape, world, ids);
        this.speeed = speeed;
    }

    public Enemy(Vector3 position, double speeed, int hp, int coolDown, int types, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, ArrayList<Integer> ids) {
        this(position, speeed, hp, hp, coolDown, types, instance, shape, 0, world, ids);
    }

    public void tick(float delta, ArrayList<Vector3> path) {
        if(spawnIndex + 1 < path.size()) {
            Vector3 tmp = new Vector3(path.get(spawnIndex + 1));
            tmp.sub(pos).setLength((float) speeed / 10f);
            instance.transform.translate(tmp);
            //System.out.println("(" + pos.x + ", " + pos.y + ", " + pos.z + ") : " + "(" + tmp.x + ", " + tmp.y + ", " + tmp.z + ") : " + pos.dst(path.get(spawnIndex + 1)) + ": " + spawnIndex);
            pos = instance.transform.getTranslation(pos);
            if(pos.dst(path.get(spawnIndex + 1)) < speeed / 10f) {
                instance.transform.setToTranslation(path.get(spawnIndex + 1));
                pos = instance.transform.getTranslation(pos);
                spawnIndex++;
            }
        } else {
            //Reached end
        }
    }

    public void render(float delta, ModelBatch modelBatch) {
        modelBatch.render(instance);
    }

    public ModelInstance getModelInstance() {
        return instance;
    }

    public Vector3 getPos() {
        return pos;
    }
}
