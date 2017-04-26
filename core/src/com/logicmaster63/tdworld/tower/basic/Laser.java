package com.logicmaster63.tdworld.tower.basic;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.object.Object;
import com.logicmaster63.tdworld.projectiles.basic.Bullet;
import com.logicmaster63.tdworld.tools.Asset;
import com.logicmaster63.tdworld.tools.Dependency;
import com.logicmaster63.tdworld.tower.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Laser extends Tower {

    public static final int HP = 20;
    public static final int TYPES = 0;
    public static final float COOLDOWN = 2f;
    public static final int RANGE = 300;
    public static final String ATTACK_ANIMATION = "";
    public static final Vector3 ATTACK_OFFSET = Vector3.Zero;

    public Laser(Vector3 pos, ModelInstance instance, btCollisionWorld world, Map<Integer, Object> objects, boolean isTemplate) {
        super(pos, HP, RANGE, COOLDOWN, TYPES, instance, new btBoxShape(instance.calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3())), world, objects, ATTACK_ANIMATION, ATTACK_OFFSET, isTemplate);
        for(int i = 0; i < instance.nodes.size; i++)
            System.out.println(instance.nodes.get(i).id);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch) {
        super.render(delta, modelBatch);

    }

    public static ArrayList<Asset> getAssets() {
        ArrayList<Asset> assets = new ArrayList<Asset>();
        assets.add(new Asset("theme/basic/tower/Laser.g3db", Model.class));
        return assets;
    }

    public static List getDependencies() {
        List<Dependency> dependencies = new ArrayList<Dependency>();
        dependencies.add(new Dependency(Bullet.class, "Bullet"));
        return dependencies;
    }
}
