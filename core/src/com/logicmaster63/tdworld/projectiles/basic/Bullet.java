package com.logicmaster63.tdworld.projectiles.basic;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.logicmaster63.tdworld.object.Object;
import com.logicmaster63.tdworld.projectiles.Projectile;
import com.logicmaster63.tdworld.tools.Asset;

import java.util.ArrayList;
import java.util.Map;

public class Bullet extends Projectile {

    public static final int HP = 20;
    public static final int LIFETIME = -1;
    public static final int TYPES = 0;
    public static final btCollisionShape SHAPE = new btSphereShape(10);
    public static final int SPEED = 10;

    public Bullet(Vector3 pos, Vector3 velocity, int hp, int health, ModelInstance model, boolean isTower, btCollisionWorld world, Map<Integer, Object> objects) {
        super(pos, velocity, hp, health, TYPES, model, SHAPE, isTower, world, objects);
    }

    public Bullet(ModelInstance model, boolean isTower, btCollisionWorld world, Map<Integer, Object> objects) {
        super(HP, TYPES, model, SHAPE, isTower, world, objects);
    }

    public Bullet(Bullet bullet, Vector3 pos, Vector3 velocity) {
        super(bullet, pos, velocity);
    }

    public static ArrayList<Asset> getAssets() {
        ArrayList<Asset> assets = new ArrayList<Asset>();
        assets.add(new Asset("theme/basic/projectile/Bullet.g3db", Model.class));
        return assets;
    }

    @Override
    public void collision(Object object) {

    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }
}
