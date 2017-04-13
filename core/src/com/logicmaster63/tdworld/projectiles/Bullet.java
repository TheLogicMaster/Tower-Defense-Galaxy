package com.logicmaster63.tdworld.projectiles;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.object.Object;

import java.util.Map;

public class Bullet extends Projectile {

    public Bullet(Vector3 pos, Vector3 velocity, int hp, int health, int types, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, Map<Integer, Object> objects) {
        super(pos, velocity, hp, health, types, model, shape, isTower, world, objects);
    }

    @Override
    public void collision(Object object) {

    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }
}
