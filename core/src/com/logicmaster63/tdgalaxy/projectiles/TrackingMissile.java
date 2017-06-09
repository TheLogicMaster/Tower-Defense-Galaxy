package com.logicmaster63.tdgalaxy.projectiles;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;

public class TrackingMissile extends Missile {

    private com.logicmaster63.tdgalaxy.entity.Entity target;

    public TrackingMissile(Vector3 pos, Vector3 velocity, com.logicmaster63.tdgalaxy.entity.Entity target, int hp, int health, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<com.logicmaster63.tdgalaxy.entity.Entity> entities, float lifetime) {
        super(pos, velocity, hp, health, types, effects, model, shape, isTower, world, entities, lifetime);
    }

    public TrackingMissile(Vector3 pos, Vector3 velocity, com.logicmaster63.tdgalaxy.entity.Entity target, int hp, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<com.logicmaster63.tdgalaxy.entity.Entity> entities, float lifetime) {
        this(pos, velocity, target, hp, hp, types, effects, model, shape, isTower, world, entities, lifetime);
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
