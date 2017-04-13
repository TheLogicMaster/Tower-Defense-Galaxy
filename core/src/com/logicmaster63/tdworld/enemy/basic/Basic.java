package com.logicmaster63.tdworld.enemy.basic;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.object.Object;

import java.util.List;
import java.util.Map;

public class Basic extends Enemy{

    public Basic(Vector3 position, double speeed, int hp, int coolDown, int types, ModelInstance instance, btCollisionWorld world, Map<Integer, Object> objects) {
        this(position, speeed, hp, hp, coolDown, types, instance, 0, world, objects);
    }

    public Basic(Vector3 pos, double speeed, int hp, int health, int coolDown, int types, ModelInstance instance, int effects, btCollisionWorld world, Map<Integer, Object> objects) {
        super(pos, speeed, hp, health, coolDown, types, instance, new btCompoundShape(), effects, world, objects);
        ((btCompoundShape)shape).addChildShape(new Matrix4(new Vector3(0, 0, 0), new Quaternion(0, 0, 0, 0), new Vector3(1, 1, 1)), new btBoxShape(new Vector3(10, 10, 10)));
        ((btCompoundShape)shape).addChildShape(new Matrix4(new Vector3(10, 0, 10), new Quaternion(0, 0, 0, 0), new Vector3(1, 1, 1)), new btBoxShape(new Vector3(10, 10, 10)));
    }

    @Override
    public void tick(float delta, List<Vector3> path) {
        super.tick(delta, path);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch) {
        super.render(delta, modelBatch);

    }

    @Override
    public boolean attack() {
        return false;
    }
}
