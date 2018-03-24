package com.logicmaster63.tdgalaxy.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.tools.Effects;
import com.logicmaster63.tdgalaxy.tools.Types;
import com.logicmaster63.tdgalaxy.enemy.Enemy;
import com.logicmaster63.tdgalaxy.projectiles.Projectile;

import java.util.EnumSet;
import java.util.Map;

public abstract class Entity implements Disposable {

    protected Vector3 tempVector, tempVector2;
    protected Matrix4 transform;
    protected int hp, health;
    protected EnumSet<Effects> effects;
    protected EnumSet<Types> types;
    protected AnimationController animation;
    protected ModelInstance instance;
    protected btCollisionShape shape;
    protected btCollisionWorld world;
    protected btCollisionObject body;
    protected Entity tempEntity;
    protected IntMap<Entity> entities;
    protected Quaternion quaternion, tempQuaternion;
    protected BoundingBox boundingBox;
    protected Map<String, Sound> sounds;
    public boolean isDead = false;

    public Entity(Matrix4 transform, int hp, int health, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities, Map<String, Sound> sounds){
        this.instance = instance;
        this.transform = transform;
        this.hp = hp;
        this.types = types;
        this.health = health;
        this.effects = effects;
        this.sounds = sounds;
        animation = new AnimationController(instance);
        this.instance.transform.set(transform);
        this.shape = shape;
        body = new btCollisionObject();
        body.setCollisionShape(shape);
        body.setWorldTransform(this.instance.transform);
        this.world = world;
        tempVector = new Vector3();
        tempVector2 = new Vector3();
        this.entities = entities;
        tempQuaternion = new Quaternion();
        quaternion = new Quaternion();
        if(this instanceof Enemy || this instanceof Projectile)
            body.setCollisionFlags(body.getCollisionFlags());
        int index = getNextIndex();
        entities.put(index, this);
        body.setUserValue(index);
        world.addCollisionObject(body);
        boundingBox = instance.calculateBoundingBox(new BoundingBox());
        //for(Node node: instance.nodes)
            //System.out.println();
    }

    public int damage(int amount) {
        hp -= amount;
        if(hp <= 0) {
            destroy();
            return amount + hp;
        }
        return amount;
    }

    public void tick(float delta) {
        instance.transform.set(transform);

        body.setWorldTransform(instance.transform);
        if(!animation.paused)
            animation.update(delta);
    }

    public void destroy() {
        world.removeCollisionObject(body);
            isDead = true;
        dispose();
    }

    protected int getNextIndex() {
        int index = 1;
        while(entities.containsKey(index))
            index++;
        return index;
    }

    protected IntMap.Entry<Entity> getEntry() {
        for(IntMap.Entry<Entity> entry: entities.entries())
            if(entry.equals(this))
                return entry;
        return null;
    }

    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer, Environment environment) {
        modelBatch.render(instance, environment);
    }

    @Override
    public void dispose() {
        body.release();
        //body.dispose();
        //shape.dispose();
    }

    public Matrix4 getTransform() {
        return transform;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": (" + transform + ", " + health + ", " + effects + ")";
    }
}
