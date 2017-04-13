package com.logicmaster63.tdworld.projectiles;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Disposable;
import com.logicmaster63.tdworld.object.Object;

import java.util.Map;

public abstract class Projectile extends Object implements Disposable{

    Vector3  velocity;
    boolean isTower;

    public Projectile(Vector3 pos, Vector3 velocity, int hp, int health, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, Map<Integer, Object> objects) {
        super(pos, hp, health, types, effects, model, shape, world, objects);
        this.isTower = isTower;
        this.velocity = velocity;
    }

    public Projectile(Vector3 pos, Vector3 velocity, int hp, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, Map<Integer, Object> objects) {
        this(pos, velocity, hp, hp, types, effects, model, shape, isTower, world, objects);
    }

    public void tick(float delta) {
        tempVector.set(velocity);
        pos.add(tempVector.scl(delta));
        super.tick(delta);
    }

    public void render(float delta, ModelBatch modelBatch) {
        pos.add(velocity.x * delta, velocity.y * delta, velocity.z * delta);
        modelBatch.render(instance);
    }

    public abstract void collision(Object object);

    @Override
    public void dispose() {
        shape.dispose();
    }
}
