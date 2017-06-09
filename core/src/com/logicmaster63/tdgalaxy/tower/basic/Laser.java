package com.logicmaster63.tdgalaxy.tower.basic;

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

public class Laser extends com.logicmaster63.tdgalaxy.tower.Tower {

    public static final int HP = 20;
    public static final int TYPES = 0;
    public static final float COOLDOWN = 1f;
    public static final int RANGE = 3000;
    public static final String ATTACK_ANIMATION = "";
    public static final Vector3 ATTACK_OFFSET = Vector3.Zero;
    private float laserTime = 0;
    private Vector3 laserTo;

    public Laser(Vector3 pos, ModelInstance instance, btCollisionWorld world, IntMap<com.logicmaster63.tdgalaxy.entity.Entity> objects, boolean isTemplate) {
        super(pos, HP, RANGE, COOLDOWN, TYPES, instance, new btBoxShape(instance.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3())), world, objects, ATTACK_ANIMATION, ATTACK_OFFSET, isTemplate);
        for(int i = 0; i < instance.nodes.size; i++)
            System.out.println(instance.nodes.get(i).id);
        laserTo = new Vector3();
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
    public void attack(ArrayList<com.logicmaster63.tdgalaxy.entity.Entity> target) {
        super.attack(target);
        laserTime = 0.5f;
        laserTo.set(target.get(0).getPos());
    }

    public static ArrayList<com.logicmaster63.tdgalaxy.tools.Asset> getAssets() {
        ArrayList<com.logicmaster63.tdgalaxy.tools.Asset> assets = new ArrayList<com.logicmaster63.tdgalaxy.tools.Asset>();
        assets.add(new com.logicmaster63.tdgalaxy.tools.Asset("theme/basic/tower/Laser.g3db", Model.class));
        return assets;
    }

    public static List getDependencies() {
        List<com.logicmaster63.tdgalaxy.tools.Dependency> dependencies = new ArrayList<com.logicmaster63.tdgalaxy.tools.Dependency>();
        dependencies.add(new com.logicmaster63.tdgalaxy.tools.Dependency(com.logicmaster63.tdgalaxy.projectiles.basic.Bullet.class, "Bullet"));
        return dependencies;
    }
}
