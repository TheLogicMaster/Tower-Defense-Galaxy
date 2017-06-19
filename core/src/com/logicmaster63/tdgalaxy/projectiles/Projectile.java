package com.logicmaster63.tdgalaxy.projectiles;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    public Projectile(Vector3 pos, Vector3 velocity, int hp, int health, float speed, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entities, float lifetime) {
        super(pos, hp, health, types, effects, model, shape, world, entities);
        this.isTower = isTower;
        this.velocity = velocity;
        this.velocity.scl(speed);
        this.lifetime = lifetime;
    }

    public void tick(float delta) {
        pos.add(tempVector.set(velocity).scl(delta));
        super.tick(delta);
        age += delta;
        if(age > lifetime)
            destroy();
    }

    @Override
    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer) {
        //pos.add(velocity.x * delta, velocity.y * delta, velocity.z * delta);
        modelBatch.render(instance);
    }

    public abstract void collision(Entity entity);
}
