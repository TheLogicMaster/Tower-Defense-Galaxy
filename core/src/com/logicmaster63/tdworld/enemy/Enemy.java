package com.logicmaster63.tdworld.enemy;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.object.AttackingObject;
import com.logicmaster63.tdworld.object.Object;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Enemy extends AttackingObject {

    private double speeed, dist = 0;
    private int moveIndex = 0;

    public Enemy(Vector3 pos, double speeed, int hp, int health, float coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, HashMap<Integer, Object> objects) {
        super(pos, hp, health, types, effects, coolDown, instance, shape, world, objects);
        this.speeed = speeed;
    }

    public Enemy(Vector3 position, double speeed, int hp, float coolDown, int types, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, HashMap<Integer, Object> objects) {
        this(position, speeed, hp, hp, coolDown, types, instance, shape, 0, world, objects);
    }

    public void tick(float delta, ArrayList<Vector3> path) {
        super.tick(delta);
        if(moveIndex + 1 < path.size()) {
            tempVector.set(path.get(moveIndex + 1));
            tempVector.sub(pos).setLength((float) speeed / 10f);
            pos.add(tempVector);
            if(pos.dst(path.get(moveIndex + 1)) < speeed / 10f) {
                pos.set(path.get(moveIndex + 1));
                moveIndex++;
            }
        } else {
            //Reached end
        }


    }

    @Override
    public void render(float delta, ModelBatch modelBatch) {
        modelBatch.render(instance);
    }

    public ModelInstance getModelInstance() {
        return instance;
    }

    public Vector3 getPos() {
        return pos;
    }

    public double getSpeeed() {
        return speeed;
    }

    public double getDist() {
        return dist;
    }
}
