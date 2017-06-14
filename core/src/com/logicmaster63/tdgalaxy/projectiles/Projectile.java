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

    Vector3 velocity;
    boolean isTower;
    protected float age;
    protected float lifetime;

    public Projectile(Vector3 pos, Vector3 velocity, int hp, int health, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entities, boolean isTemplate, float lifetime) {
        super(pos, hp, health, types, effects, model, shape, world, entities, isTemplate);
        this.isTower = isTower;
        this.velocity = velocity;
    }

    //Template constructor
    public Projectile(int hp, EnumSet<Types> types, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entities, float lifetime) {
        this(Vector3.Zero, Vector3.Zero, hp, hp, types, EnumSet.noneOf(Effects.class), model, shape, isTower, world, entities, true, lifetime);
    }

    //Copy constructor
    public Projectile(Projectile projectile, Vector3 pos, Vector3 velocity) {
        this(pos, velocity, projectile.hp, projectile.health, projectile.types, projectile.effects, new ModelInstance(projectile.instance), projectile.shape, projectile.isTower, projectile.world, projectile.entities, false, projectile.lifetime);
    }

    public void tick(float delta) {
        tempVector.set(velocity);
        pos.add(tempVector.scl(delta));
        super.tick(delta);
        age += delta;
    }

    @Override
    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer) {
        //pos.add(velocity.x * delta, velocity.y * delta, velocity.z * delta);
        modelBatch.render(instance);
    }

    public abstract void collision(Entity entity);

    @Override
    public void dispose() {
        shape.dispose();
    }
}
