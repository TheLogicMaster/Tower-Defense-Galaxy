package com.logicmaster63.tdworld.enemy.basic;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.logicmaster63.tdworld.enemy.Enemy;

public class Basic extends Enemy {

    public Basic(Vector3 position, double speeed, int hp, int coolDown, int types, ModelInstance instance, btCollisionShape shape) {
        super(position, speeed, hp, coolDown, types, instance, shape);
    }

    public Basic(Vector3 pos, double speeed, int hp, int health, int coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects) {
        super(pos, speeed, hp, health, coolDown, types, instance, shape, effects);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch) {
        super.render(delta, modelBatch);

    }
}
