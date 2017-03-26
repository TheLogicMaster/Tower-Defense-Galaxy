package com.logicmaster63.tdworld.projectiles;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public class Bullet extends Projectile {

    public Bullet(Vector3 pos, Vector3 velocity, int hp, int health, int types, ModelInstance model, btCollisionShape shape, boolean isTower) {
        super(pos, velocity, hp, health, types, model, shape, isTower);
    }

    @Override
    public void tick(float delta) {

    }
}
