package com.logicmaster63.tdworld.projectiles;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.object.Object;

import java.util.Map;

public class TrackingMissile extends Missile {

    private Object target;

    public TrackingMissile(Vector3 pos, Vector3 velocity, Object target, int hp, int health, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, Map<Integer, Object> objects) {
        super(pos, velocity, hp, health, types, effects, model, shape, isTower, world, objects);
    }

    public TrackingMissile(Vector3 pos, Vector3 velocity, Object target, int hp, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, Map<Integer, Object> objects) {
        this(pos, velocity, target, hp, hp, types, effects, model, shape, isTower, world, objects);
    }

    @Override
    public void tick(float delta) {
        if(target != null) {
            tempVector.set(target.getPos());
            velocity.set(tempVector.sub(pos).setLength(velocity.len()));
        }
        super.tick(delta);
    }
}
