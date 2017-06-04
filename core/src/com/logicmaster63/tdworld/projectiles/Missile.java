package com.logicmaster63.tdworld.projectiles;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdworld.entity.Entity;

import java.util.Map;

public class Missile extends Projectile {

    public Missile(Vector3 pos, Vector3 velocity, int hp, int health, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entities, float lifetime) {
        super(pos, velocity, hp, types, effects, model, shape, isTower, world, entities, lifetime);
    }

    public Missile(Vector3 pos, Vector3 velocity, int hp, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entites, float lifetime) {
        this(pos, velocity, hp, hp, types, effects, model, shape, isTower, world, entites, lifetime);
    }

    @Override
    public void collision(Entity entity) {

    }
}
