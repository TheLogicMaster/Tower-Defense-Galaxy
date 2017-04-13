package com.logicmaster63.tdworld.tower;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.object.AttackingObject;
import com.logicmaster63.tdworld.object.Object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Tower extends AttackingObject{

    public Tower(Vector3 pos, int hp, float coolDown, int types, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, Map<Integer, Object> objects) {
        this(pos, hp, hp, coolDown, types, instance, shape, 0, world, objects);
    }

    public Tower(Vector3 pos, int hp, float coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, Map<Integer, Object> objects) {
        this(pos, hp, hp, coolDown, types, instance, shape, effects, world, objects);
    }

    public Tower(Vector3 pos, int hp, int health, float coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, Map<Integer, Object> objects) {
        super(pos, hp, health, types, effects, coolDown, instance, shape, world, objects);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        if(coolTime > coolDown && attack())
            coolTime = 0;
    }

    @Override
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
