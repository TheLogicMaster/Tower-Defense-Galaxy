package com.logicmaster63.tdgalaxy.tower.basic;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.tools.Effects;
import com.logicmaster63.tdgalaxy.tools.Source;
import com.logicmaster63.tdgalaxy.tools.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.tools.Asset;
import com.logicmaster63.tdgalaxy.tower.Tower;

import java.util.*;

public class Laser extends Tower {

    public static final int PRICE = 40;
    public static final int HP = 20;
    public static final EnumSet<Types> TYPES = EnumSet.of(Types.FIRE);
    public static final float COOLDOWN = 1f;
    public static final int RANGE = 3000;
    public static final int LASER_RANGE = 3000;
    public static final String ATTACK_ANIMATION = "";
    public static final Vector3 ATTACK_OFFSET = Vector3.Zero;

    private float laserTime = 0;
    private float laserRange;
    private Vector3 laserTo;

    //Debug constructor
    public Laser(Matrix4 transform, int hp, int health, int range, int laserRange, float cooldown, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> objects, String animation, Vector3 attackOffset, Map<String, Sound> sounds) {
        super(transform, hp, health, range, cooldown, types, effects, instance, shape, world, objects, animation, attackOffset, sounds);
        laserTo = new Vector3();
        this.laserRange = laserRange;
    }

    public Laser(Matrix4 transform, Map<String, Model> models, btCollisionWorld world, IntMap<Entity> objects, Map<String, Sound> sounds) {
        this(transform, HP, HP, RANGE, LASER_RANGE, COOLDOWN, TYPES, EnumSet.noneOf(Effects.class), new ModelInstance(models.get("Laser")), new btBoxShape(models.get("Laser").calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3())), world, objects, ATTACK_ANIMATION, ATTACK_OFFSET, sounds);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer, Environment environment) {
        super.render(delta, modelBatch, shapeRenderer, environment);
        if(laserTime > 0) {
            laserTime -= delta;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 0, 0, 1);
            shapeRenderer.line(transform.getTranslation(tempVector), laserTo);
            shapeRenderer.end();
        }
    }

    @Override
    public void attack(ArrayList<Entity> target) {
        super.attack(target);
        laserTime = 0.5f;
        laserTo.set(target.get(0).getTransform().getTranslation(tempVector)).setLength(laserRange);
    }

    public static List<Asset> getAssets() {
        return Arrays.asList(new Asset(Source.INTERNAL,"theme/basic/tower/Laser.g3db", Model.class));
    }
}
