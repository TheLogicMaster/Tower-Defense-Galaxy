package com.logicmaster63.tdgalaxy.projectiles;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;

import java.util.EnumSet;

public abstract class Projectile extends Entity {

    protected Vector3 velocity;
    protected boolean isTower;
    protected float age;
    protected float lifetime;

    public Projectile(Matrix4 transform, Vector3 velocity, int hp, int health, float speed, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entities, float lifetime) {
        super(transform, hp, health, types, effects, model, shape, world, entities);
        this.isTower = isTower;
        this.velocity = velocity;
        this.velocity.scl(speed);
        this.lifetime = lifetime;
    }

    public void tick(float delta) {
        transform.translate(tempVector.set(velocity).scl(delta));
        //pos.add(tempVector.set(velocity).scl(delta));
        super.tick(delta);
        age += delta;
        if(age > lifetime)
            destroy();
    }

    public abstract void collision(Entity entity);
}
