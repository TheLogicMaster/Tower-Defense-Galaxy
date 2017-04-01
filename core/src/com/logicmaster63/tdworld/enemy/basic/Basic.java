package com.logicmaster63.tdworld.enemy.basic;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.screens.GameScreen;

import java.util.ArrayList;

public class Basic extends Enemy{

    public Basic(Vector3 position, double speeed, int hp, int coolDown, int types, ModelInstance instance, btCollisionWorld world, ArrayList<Integer> ids) {
        this(position, speeed, hp, hp, coolDown, types, instance, 0, world, ids);
    }

    public Basic(Vector3 pos, double speeed, int hp, int health, int coolDown, int types, ModelInstance instance, int effects, btCollisionWorld world, ArrayList<Integer> ids) {
        super(pos, speeed, hp, health, coolDown, types, instance, new btBoxShape(instance.extendBoundingBox(new BoundingBox()).getDimensions(new Vector3())), effects, world, ids);
    }

    @Override
    public void tick(float delta, ArrayList<Vector3> path) {
        super.tick(delta, path);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch) {
        super.render(delta, modelBatch);

    }
}
