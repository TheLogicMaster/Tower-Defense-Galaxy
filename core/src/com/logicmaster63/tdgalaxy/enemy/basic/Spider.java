package com.logicmaster63.tdgalaxy.enemy.basic;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.enemy.Enemy;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.tools.Asset;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class Spider extends Enemy {

    public static final EnumSet<Types> TYPES = EnumSet.of(Types.sharp);
    public static final int HP = 20;
    public static final float COOLDOWN = 0.3f;
    public static final int RANGE = 1000;
    public static final String ATTACK_ANIMATION = "";
    public static final Vector3 ATTACK_OFFSET = Vector3.Zero;

    public Spider(Matrix4 transform, double speeed, int hp, int health, int range, float coolDown, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionWorld world, IntMap<Entity> entities, List<Vector3> path) {
        super(transform, speeed, hp, health, range, coolDown, types, effects, instance, new btBoxShape(instance.extendBoundingBox(new BoundingBox()).getDimensions(new Vector3())), world, entities, ATTACK_ANIMATION, ATTACK_OFFSET, path);
    }

    public Spider(Matrix4 transform, double speeed, Map<String, Model> models, btCollisionWorld world, IntMap<Entity> entities, List<Vector3> path) {
        this(transform, speeed, HP, HP, RANGE, COOLDOWN, TYPES, EnumSet.noneOf(Effects.class), new ModelInstance(models.get("Basic")), world, entities, path);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }

    public static ArrayList<com.logicmaster63.tdgalaxy.tools.Asset> getAssets() {
        ArrayList<Asset> assets = new ArrayList<Asset>();
        assets.add(new Asset("theme/basic/enemy/Basic.g3db", Model.class));
        return assets;
    }
}
