package com.logicmaster63.tdgalaxy.projectiles;

import com.badlogic.gdx.audio.Sound;
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
import java.util.Map;
import java.util.Set;

public class TrackingMissile extends Missile {

    private Entity target;

    public TrackingMissile(Matrix4 transform, Vector3 velocity, Entity target, int hp, int health, float speed, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entities, float lifetime, Map<String, Sound> sounds) {
        super(transform, velocity, hp, health, speed, types, effects, model, shape, isTower, world, entities, lifetime, sounds);
    }

    public TrackingMissile(Matrix4 transform, Vector3 velocity, float speed, Entity target, int hp, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entities, float lifetime, Map<String, Sound> sounds) {
        this(transform, velocity, target, hp, hp, speed, types, effects, model, shape, isTower, world, entities, lifetime, sounds);
    }

    @Override
    public void tick(float delta) {
        if(target != null) {
            target.getTransform().getTranslation(tempVector);
            velocity.set(tempVector.sub(transform.getTranslation(tempVector2)).setLength(velocity.len()));
        }
        super.tick(delta);
    }
}
