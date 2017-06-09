package com.logicmaster63.tdgalaxy.projectiles.basic;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.IntMap;

import java.util.ArrayList;

public class Bullet extends com.logicmaster63.tdgalaxy.projectiles.Projectile {

    public static final int HP = 20;
    public static final int LIFETIME = 10;
    public static final int TYPES = 0;
    public static final btCollisionShape SHAPE = new btSphereShape(10);
    public static final int SPEED = 10;

    public Bullet(Vector3 pos, Vector3 velocity, int hp, int health, ModelInstance model, boolean isTower, btCollisionWorld world, IntMap<com.logicmaster63.tdgalaxy.entity.Entity> entities) {
        super(pos, velocity, hp, health, TYPES, model, SHAPE, isTower, world, entities, LIFETIME);
    }

    public Bullet(ModelInstance model, boolean isTower, btCollisionWorld world, IntMap<com.logicmaster63.tdgalaxy.entity.Entity> entities) {
        super(HP, TYPES, model, SHAPE, isTower, world, entities, LIFETIME);
    }

    public Bullet(Bullet bullet, Vector3 pos, Vector3 velocity) {
        super(bullet, pos, velocity);
    }

    public static ArrayList<com.logicmaster63.tdgalaxy.tools.Asset> getAssets() {
        ArrayList<com.logicmaster63.tdgalaxy.tools.Asset> assets = new ArrayList<com.logicmaster63.tdgalaxy.tools.Asset>();
        assets.add(new com.logicmaster63.tdgalaxy.tools.Asset("theme/basic/projectile/Bullet.g3db", Model.class));
        return assets;
    }

    @Override
    public void collision(com.logicmaster63.tdgalaxy.entity.Entity entity) {

    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }
}
