package com.logicmaster63.tdworld.enemy.basic;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.object.Object;

import java.util.List;
import java.util.Map;

public class Spider extends Enemy {

    public Spider(Vector3 position, double speeed, int hp, int coolDown, int types, ModelInstance instance, btCollisionWorld world, Map<Integer, Object> objects) {
        this(position, speeed, hp, hp, coolDown, types, instance, 0, world, objects);
    }

    public Spider(Vector3 pos, double speeed, int hp, int health, int coolDown, int types, ModelInstance instance, int effects, btCollisionWorld world, Map<Integer, Object> objects) {
        super(pos, speeed, hp, health, coolDown, types, instance, new btBoxShape(instance.extendBoundingBox(new BoundingBox()).getDimensions(new Vector3())), effects, world, objects);
    }

    @Override
    public boolean attack() {

        return false;
    }

    @Override
    public void tick(float delta, List<Vector3> path) {
        super.tick(delta, path);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch) {
        super.render(delta, modelBatch);
    }
}
