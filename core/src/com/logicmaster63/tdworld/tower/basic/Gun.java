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

public class Gun extends Tower {

    private static final int HP = 20;
    private static final int COOLDOWN = 1000;

    public Gun(Vector3 pos, int types, ModelInstance instance, btCollisionWorld world, ArrayList<Integer> ids) {
        super(pos, HP, COOLDOWN, types, instance, new btBoxShape(instance.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3())), world, ids);
    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public void render(float delta, ModelBatch modelBatch) {
        super.render(delta, modelBatch);

    }
}
