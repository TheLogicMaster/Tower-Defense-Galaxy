package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Disposable;
import com.logicmaster63.tdworld.screens.GameScreen;

import java.util.ArrayList;

public abstract class Object implements Disposable{

    protected Vector3 pos;
    protected int hp, health, effects, coolDown, types;
    protected AnimationController animation;
    protected ModelInstance instance;
    protected btCollisionShape shape;

    public Object(Vector3 pos, int hp, int health, int coolDown, int types, int effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, ArrayList<Integer> ids){
        this.instance = instance;
        this.pos = pos;
        this.hp = hp;
        this.coolDown = coolDown;
        this.types = types;
        this.health = health;
        this.effects = effects;
        animation = new AnimationController(instance);
        this.instance.transform.setTranslation(pos);
        this.shape = shape;
        btCollisionObject body = new btCollisionObject();
        body.setCollisionShape(shape);
        body.setWorldTransform(instance.transform);
        world.addCollisionObject(body);
    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}
