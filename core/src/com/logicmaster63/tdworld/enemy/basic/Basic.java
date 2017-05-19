package com.logicmaster63.tdworld.enemy.basic;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.entity.Entity;
import com.logicmaster63.tdworld.tools.Asset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Basic extends Enemy{

    public static final int HP = 20;
    public static final float COOLDOWN = 1f;
    public static final int RANGE = 300;
    public static final String ATTACK_ANIMATION = "Spider_Armature|Attack";
    public static final int TYPES = 0;
    public static final double SPEED = 10;
    public static final Vector3 ATTACK_OFFSET = Vector3.Zero;

    private AnimationController.AnimationListener listener;

    public Basic(Vector3 position, ModelInstance instance, btCollisionWorld world, Map<Integer, Entity> objects, boolean isTemplate, List<Vector3> path) {
        this(position, SPEED, HP, COOLDOWN, TYPES, instance, 0, world, objects, isTemplate, path);
    }

    public Basic(Vector3 pos, double speeed, int health, float coolDown, int types, ModelInstance instance, int effects, btCollisionWorld world, Map<Integer, Entity> objects, boolean isTemplate, List<Vector3> path) {
        super(pos, speeed, HP, health, RANGE, coolDown, types, instance, new btCompoundShape(), effects, world, objects, ATTACK_ANIMATION, ATTACK_OFFSET, isTemplate, path);
        ((btCompoundShape)shape).addChildShape(new Matrix4(new Vector3(0, 30, 0), new Quaternion().setEulerAngles(0, 0, 0), new Vector3(1, 1, 1)), new btBoxShape(new Vector3(75, 30, 90)));
        //System.out.println(getModelInstance().getAnimation("Spider_Armature|walk_ani_vor").id);
        listener = new AnimationController.AnimationListener() {
            @Override
            public void onEnd(AnimationController.AnimationDesc animationDesc) {

            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animationDesc) {

            }
        };
        //animation.setAnimation("Spider_Armature|walk_ani_vor");
        //animation.animate("Spider_Armature|walk_ani_vor", -1);
        //animation.action("Spider_Armature|walk_ani_vor", 0, 1000, -1, 1, listener, 0);
        //animation.animate("Spider_Armature|Attack", 0, 1000, 1, 1, listener, 0);
        //animation.queue("Spider_Armature|walk_ani_vor", 0, 1000, -1, 1, listener, 0);
    }

    public static ArrayList<Asset> getAssets() {
        ArrayList<Asset> assets = new ArrayList<Asset>();
        assets.add(new Asset("theme/basic/enemy/Basic.g3db", Model.class));
        return assets;
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer) {
        super.render(delta, modelBatch, shapeRenderer);
    }
}
