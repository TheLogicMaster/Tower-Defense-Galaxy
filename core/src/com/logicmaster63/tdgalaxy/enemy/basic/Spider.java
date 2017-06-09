package com.logicmaster63.tdgalaxy.enemy.basic;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;

import java.util.ArrayList;
import java.util.List;

public class Spider extends com.logicmaster63.tdgalaxy.enemy.Enemy {

    public static final int HP = 20;
    public static final float COOLDOWN = 0.3f;
    public static final int RANGE = 1000;
    public static final String ATTACK_ANIMATION = "";
    public static final Vector3 ATTACK_OFFSET = Vector3.Zero;

    public Spider(Vector3 position, double speeed, int types, ModelInstance instance, btCollisionWorld world, IntMap<com.logicmaster63.tdgalaxy.entity.Entity> entities, boolean isTemplate, List<Vector3> path) {
        this(position, speeed, HP, COOLDOWN, types, instance, 0, world, entities, isTemplate, path);
    }

    public Spider(Vector3 pos, double speeed, int health, float coolDown, int types, ModelInstance instance, int effects, btCollisionWorld world, IntMap<com.logicmaster63.tdgalaxy.entity.Entity> entities, boolean isTemplate, List<Vector3> path) {
        super(pos, speeed, HP, health, RANGE, coolDown, types, instance, new btBoxShape(instance.extendBoundingBox(new BoundingBox()).getDimensions(new Vector3())), effects, world, entities, ATTACK_ANIMATION, ATTACK_OFFSET, isTemplate, path);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer) {
        super.render(delta, modelBatch, shapeRenderer);
    }

    public static ArrayList<com.logicmaster63.tdgalaxy.tools.Asset> getAssets() {
        ArrayList<com.logicmaster63.tdgalaxy.tools.Asset> assets = new ArrayList<com.logicmaster63.tdgalaxy.tools.Asset>();
        assets.add(new com.logicmaster63.tdgalaxy.tools.Asset("theme/basic/enemy/Basic.g3db", Model.class));
        return assets;
    }
}
