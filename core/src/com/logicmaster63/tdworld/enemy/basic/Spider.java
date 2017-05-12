package com.logicmaster63.tdworld.enemy.basic;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.enums.TargetMode;
import com.logicmaster63.tdworld.object.Object;
import com.logicmaster63.tdworld.tools.Asset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Spider extends Enemy {

    public static final int HP = 20;
    public static final float COOLDOWN = 0.3f;
    public static final int RANGE = 1000;
    public static final String ATTACK_ANIMATION = "";
    public static final Vector3 ATTACK_OFFSET = Vector3.Zero;

    public Spider(Vector3 position, double speeed, int types, ModelInstance instance, btCollisionWorld world, Map<Integer, Object> objects, boolean isTemplate, List<Vector3> path) {
        this(position, speeed, HP, COOLDOWN, types, instance, 0, world, objects, isTemplate, path);
    }

    public Spider(Vector3 pos, double speeed, int health, float coolDown, int types, ModelInstance instance, int effects, btCollisionWorld world, Map<Integer, Object> objects, boolean isTemplate, List<Vector3> path) {
        super(pos, speeed, HP, health, RANGE, coolDown, types, instance, new btBoxShape(instance.extendBoundingBox(new BoundingBox()).getDimensions(new Vector3())), effects, world, objects, ATTACK_ANIMATION, ATTACK_OFFSET, isTemplate, path);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer) {
        super.render(delta, modelBatch, shapeRenderer);
    }

    public static ArrayList<Asset> getAssets() {
        ArrayList<Asset> assets = new ArrayList<Asset>();
        assets.add(new Asset("theme/basic/enemy/Basic.g3db", Model.class));
        return assets;
    }
}
