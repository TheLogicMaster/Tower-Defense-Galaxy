package com.logicmaster63.tdworld.tower;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.object.Object;

import java.util.Map;

public abstract class TargettingTower extends Tower{

    protected Object target;

    public TargettingTower(Vector3 pos, int hp, float coolDown, int types, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, Map<Integer, Object> objects) {
        super(pos, hp, coolDown, types, instance, shape, world, objects);
    }

    public TargettingTower(Vector3 pos, int hp, float coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, Map<Integer, Object> objects) {
        super(pos, hp, coolDown, types, instance, shape, effects, world, objects);
    }
}
