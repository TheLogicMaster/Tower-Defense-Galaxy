package com.logicmaster63.tdgalaxy.projectiles;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;

import java.util.EnumSet;

public class Missile extends Projectile {

    public Missile(Vector3 pos, Vector3 velocity, int hp, int health, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entities,  float lifetime) {
        super(pos, velocity, hp, health, types, effects, model, shape, isTower, world, entities,false, lifetime);
    }

    public Missile(Vector3 pos, Vector3 velocity, int hp, EnumSet<Types> types, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entites, float lifetime) {
        this(pos, velocity, hp, hp, types, EnumSet.noneOf(Effects.class), model, shape, isTower, world, entites, lifetime);
    }

    @Override
    public void collision(Entity entity) {

    }
}
