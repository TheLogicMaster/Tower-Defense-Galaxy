package com.logicmaster63.tdworld.projectiles;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.object.Object;

import java.util.HashMap;

public class Missile extends Projectile {

    public Missile(Vector3 pos, Vector3 velocity, int hp, int health, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, HashMap<Integer, Object> objects) {
        super(pos, velocity, hp, health, types, effects, model, shape, isTower, world, objects);
    }

    public Missile(Vector3 pos, Vector3 velocity, int hp, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, HashMap<Integer, Object> objects) {
        this(pos, velocity, hp, hp, types, effects, model, shape, isTower, world, objects);
    }

    @Override
    public void collision(Object object) {

    }
}
