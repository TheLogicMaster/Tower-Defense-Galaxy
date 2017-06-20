package com.logicmaster63.tdgalaxy.traps;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;

import java.util.EnumSet;

public abstract class Trap extends Entity {

    public Trap(Matrix4 transform, int hp, int health, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities) {
        super(transform, hp, health, types, effects, instance, shape, world, entities);
    }

    public abstract void collision(Entity entity);
}
