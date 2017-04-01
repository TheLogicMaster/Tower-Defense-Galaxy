package com.logicmaster63.tdworld.tower.basic;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.screens.GameScreen;
import com.logicmaster63.tdworld.tower.Tower;

import java.util.ArrayList;

public class Laser extends Tower {

    public Laser(Vector3 pos, int hp, int health, int coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, ArrayList<Integer> ids) {
        super(pos, hp, coolDown, types, instance, shape, effects, world, ids);
    }

    public Laser(Vector3 pos, int hp, int coolDown, int types, ModelInstance instance, btCollisionWorld world, ArrayList<Integer> ids) {
        this(pos, hp, hp, coolDown, types, instance, new btBoxShape(instance.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3())), 0, world, ids);
    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public void render(float delta, ModelBatch modelBatch) {
        super.render(delta, modelBatch);

    }
}
