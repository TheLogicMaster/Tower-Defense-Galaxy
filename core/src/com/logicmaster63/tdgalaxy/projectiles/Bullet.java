package com.logicmaster63.tdgalaxy.projectiles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Source;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.tools.Asset;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class Bullet extends Projectile {

    private static final int HP = 20;
    private static final float LIFETIME = 10;
    private static final EnumSet<Types> TYPES = EnumSet.of(Types.sharp);
    private static final btCollisionShape SHAPE = new btSphereShape(10);
    private static final float SPEED = 300;

    //Debug constructor
    public Bullet(Matrix4 transform, Vector3 velocity, int hp, int health, float speed, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<Entity> entities, float lifetime, Map<String, Sound> sounds) {
        super(transform, velocity, hp, health, speed, types, effects, model, shape, isTower, world, entities, lifetime, sounds);
    }

    public Bullet(Matrix4 transform, Vector3 velocity, Map<String, Model> models, boolean isTower, btCollisionWorld world, IntMap<Entity> entities, Map<String, Sound> sounds) {
        super(transform, velocity, HP, HP, SPEED, TYPES, EnumSet.noneOf(Effects.class), new ModelInstance(models.get("Bullet")), SHAPE, isTower, world, entities, LIFETIME, sounds);
    }

    public static ArrayList<Asset> getAssets() {
        ArrayList<Asset> assets = new ArrayList<com.logicmaster63.tdgalaxy.tools.Asset>();
        assets.add(new Asset(Source.INTERNAL,"theme/basic/projectile/Bullet.g3db", Model.class));
        return assets;
    }

    @Override
    public void collision(Entity entity) {

    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }
}
