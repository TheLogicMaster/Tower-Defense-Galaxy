package com.logicmaster63.tdworld.enemy;


import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public abstract class Boss extends Enemy{

    public Boss(Vector3 position, double speeed, int hp, int coolDown, int types, ModelInstance instance, btCollisionShape shape) {
        super(position, speeed, hp, coolDown, types, instance, shape);
    }
}
