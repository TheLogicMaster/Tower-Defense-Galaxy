package com.logicmaster63.tdworld.enemy;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Disposable;
import com.logicmaster63.tdworld.tools.Object;

public abstract class Enemy extends Object {

    private double speeed;
    private ModelInstance instance;
    private btCollisionObject body;

    public Enemy(Vector3 pos, double speeed, int hp, int health, int coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects) {
        this.pos = pos;
        this.speeed = speeed;
        this.instance = instance;
        this.instance.transform.setTranslation(pos);
        body = new btCollisionObject();
        body.setCollisionShape(shape);
        shape.dispose();
        this.effects = effects;
        this.hp = hp;
        this.health = health;
        this.coolDown = coolDown;
        this.types = types;
    }

    public Enemy(Vector3 position, double speeed, int hp, int coolDown, int types, ModelInstance instance, btCollisionShape shape) {
        this(position, speeed, hp, hp, coolDown, types, instance, shape, 0);
    }

    public abstract void tick(float delta);

    public void render(float delta, ModelBatch modelBatch) {
        modelBatch.render(instance);
    }

    @Override
    public void dispose() {
        body.dispose();
    }
}
