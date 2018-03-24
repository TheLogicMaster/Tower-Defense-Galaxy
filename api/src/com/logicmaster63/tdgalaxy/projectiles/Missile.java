package com.logicmaster63.tdgalaxy.projectiles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.tools.Effects;
import com.logicmaster63.tdgalaxy.tools.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;

import java.util.EnumSet;
import java.util.Map;

public class Missile extends Projectile {

    public Missile(Matrix4 transform, Vector3 velocity, int hp, int health, float speed, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entities, float lifetime, Map<String, Sound> sounds) {
        super(transform, velocity, hp, health, speed, types, effects, model, shape, isTower, world, entities, lifetime, sounds);
    }

    public Missile(Matrix4 transform, Vector3 velocity, int hp, int speed, EnumSet<Types> types, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entites, float lifetime, Map<String, Sound> sounds) {
        this(transform, velocity, hp, hp, speed, types, EnumSet.noneOf(Effects.class), model, shape, isTower, world, entites, lifetime, sounds);
    }

    @Override
    public void collision(Entity entity) {

    }
}
