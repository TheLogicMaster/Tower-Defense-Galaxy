package com.logicmaster63.tdworld.projectiles;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Disposable;
import com.logicmaster63.tdworld.tools.Object;

public class Projectile implements Disposable{

    private Vector3 pos, velocity;
    private int hp, health, types;
    private ModelInstance model;
    private btCollisionShape shape;
    private boolean isTower;

    public Projectile(Vector3 pos, Vector3 velocity, int hp, int health, int types, ModelInstance model, btCollisionShape shape, boolean isTower) {
        this.pos = pos;
        this.velocity = velocity;
        this.hp = hp;
        this.model = model;
        this.shape = shape;
    }

    public Projectile(Vector3 pos, Vector3 velocity, int hp, int types, ModelInstance model, btCollisionShape shape, boolean isTower) {
        this(pos, velocity, hp, hp, types, model, shape, isTower);
    }

    public void render(float delta, ModelBatch modelBatch) {
        pos.add(velocity.x * delta, velocity.y * delta, velocity.z * delta);
        modelBatch.render(model);
    }

    public boolean collision(Object object) {
        return true; //change to represent whether or not it breaks
    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}
