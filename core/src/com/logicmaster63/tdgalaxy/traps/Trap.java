package com.logicmaster63.tdgalaxy.traps;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.entity.Entity;

public abstract class Trap extends Entity {

    public Trap(Vector3 pos, int hp, int health, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, IntMap<Entity> entities, boolean isTemplate) {
        super(pos, hp, health, types, effects, instance, shape, world, entities, isTemplate);
    }

    public abstract void collision(Entity entity);
}
