package com.logicmaster63.tdworld.enemy.basic;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.enums.TargetMode;
import com.logicmaster63.tdworld.object.Object;

import java.util.List;
import java.util.Map;

public class Spider extends Enemy {

    private static final int HP = 20;
    private static final float COOLDOWN = 0.3f;
    private static final int RANGE = 1000;
    private static final String ATTACK_ANIMATION = "";

    public Spider(Vector3 position, double speeed, int types, ModelInstance instance, btCollisionWorld world, Map<Integer, Object> objects) {
        this(position, speeed, HP, COOLDOWN, types, instance, 0, world, objects);
    }

    public Spider(Vector3 pos, double speeed, int health, float coolDown, int types, ModelInstance instance, int effects, btCollisionWorld world, Map<Integer, Object> objects) {
        super(pos, speeed, HP, health, RANGE, coolDown, types, instance, new btBoxShape(instance.extendBoundingBox(new BoundingBox()).getDimensions(new Vector3())), effects, world, objects, ATTACK_ANIMATION);
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
