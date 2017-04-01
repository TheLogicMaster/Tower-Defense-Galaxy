package com.logicmaster63.tdworld.tower;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Disposable;
import com.logicmaster63.tdworld.screens.GameScreen;
import com.logicmaster63.tdworld.tools.Object;

import java.util.ArrayList;

public abstract class Tower extends Object{

    public Tower(Vector3 pos, int hp, int coolDown, int types, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, ArrayList<Integer> ids) {
        this(pos, hp, hp, coolDown, types, instance, shape, 0, world, ids);
    }

    public Tower(Vector3 pos, int hp, int coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, ArrayList<Integer> ids) {
        this(pos, hp, hp, coolDown, types, instance, shape, effects, world, ids);
    }

    public Tower(Vector3 pos, int hp, int health, int coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, ArrayList<Integer> ids) {
        super(pos, hp, health, coolDown, types, effects, instance, shape, world, ids);
    }

    public abstract void tick(float delta);

    public void render(float delta, ModelBatch modelBatch) {
        //for(int i = 0; i < instance.nodes.size; i++)
            //Gdx.app.log(Double.toString(position.x), instance.nodes.get(i).id);
        //instance.nodes.get(0).rotation.set(Vector3.Y, (instance.nodes.get(0).rotation.getAngle() + 1) % 360);
        //instance.nodes.get(0).inheritTransform = false;
        //Gdx.app.log(Double.toString(position.x), Float.toString(instance.nodes.get(0).rotation.getAngle()));
        //instance.transform.rotate(Vector3.X, 1);
        modelBatch.render(instance);
    }

    @Override
    public void dispose() {

    }
}
