package com.logicmaster63.tdworld.object;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AttackingObject extends Object{

    protected float coolDown, coolTime = 0;

    public AttackingObject(Vector3 pos, int hp, int health, int types, int effects, float coolDown, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, HashMap<Integer, Object> objects){
        super(pos, hp, health, types, effects, instance, shape, world, objects);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        coolTime += delta;
    }

    public abstract boolean attack();
}
