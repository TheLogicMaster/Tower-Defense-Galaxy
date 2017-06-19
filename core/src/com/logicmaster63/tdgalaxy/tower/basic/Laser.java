package com.logicmaster63.tdgalaxy.tower.basic;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.projectiles.Bullet;
import com.logicmaster63.tdgalaxy.tools.Asset;
import com.logicmaster63.tdgalaxy.tools.Dependency;
import com.logicmaster63.tdgalaxy.tower.Tower;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class Laser extends Tower {

    public static final int HP = 20;
    public static final EnumSet<Types> TYPES = EnumSet.of(Types.fire);
    public static final float COOLDOWN = 1f;
    public static final int RANGE = 3000;
    public static final int LASER_RANGE = 3000;
    public static final String ATTACK_ANIMATION = "";
    public static final Vector3 ATTACK_OFFSET = Vector3.Zero;

    private float laserTime = 0;
    private float laserRange;
    private Vector3 laserTo;

    //Debug constructor
    public Laser(Vector3 pos, int hp, int health, int range, int laserRange, float cooldown, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> objects, String animation, Vector3 attackOffset) {
        super(pos, hp, health, range, cooldown, types, effects, instance, shape, world, objects, animation, attackOffset);
        for(int i = 0; i < instance.nodes.size; i++)
            System.out.println(instance.nodes.get(i).id);
        laserTo = new Vector3();
        this.laserRange = laserRange;
    }

    public Laser(Vector3 pos, Map<String, Model> models, btCollisionWorld world, IntMap<Entity> objects) {
        this(pos, HP, HP, RANGE, LASER_RANGE, COOLDOWN, TYPES, EnumSet.noneOf(Effects.class), new ModelInstance(models.get("Laser")), new btBoxShape(models.get("Laser").calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3())), world, objects, ATTACK_ANIMATION, ATTACK_OFFSET);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer) {
        super.render(delta, modelBatch, shapeRenderer);
        if(laserTime > 0) {
            laserTime -= delta;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 0, 0, 1);
            shapeRenderer.line(pos, laserTo);
            shapeRenderer.end();
        }
    }

    @Override
    public void attack(ArrayList<Entity> target) {
        super.attack(target);
        laserTime = 0.5f;
        laserTo.set(target.get(0).getPos()).setLength(laserRange);
    }

    public static ArrayList<Asset> getAssets() {
        ArrayList<Asset> assets = new ArrayList<com.logicmaster63.tdgalaxy.tools.Asset>();
        assets.add(new Asset("theme/basic/tower/Laser.g3db", Model.class));
        return assets;
    }

    public static List getDependencies() {
        List<Dependency> dependencies = new ArrayList<Dependency>();
        dependencies.add(new Dependency(Bullet.class, "Bullet"));
        return dependencies;
    }
}
