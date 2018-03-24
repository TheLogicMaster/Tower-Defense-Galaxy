package com.logicmaster63.tdgalaxy.traps;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.tools.Effects;
import com.logicmaster63.tdgalaxy.tools.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;

import java.util.EnumSet;
import java.util.Map;

public abstract class Trap extends Entity {

    public Trap(Matrix4 transform, int hp, int health, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities, Map<String, Sound> sounds) {
        super(transform, hp, health, types, effects, instance, shape, world, entities, sounds);
    }

    public abstract void collision(Entity entity);
}
