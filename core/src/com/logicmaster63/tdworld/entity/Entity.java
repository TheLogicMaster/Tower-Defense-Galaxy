package com.logicmaster63.tdworld.entity;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.projectiles.Projectile;

import java.util.Map;

public abstract class Entity implements Disposable{

    protected Vector3 pos, tempVector;
    protected int hp, health, effects, types;
    protected AnimationController animation;
    protected ModelInstance instance;
    protected btCollisionShape shape;
    protected btCollisionWorld world;
    protected btCollisionObject body;
    protected Entity tempEntity;
    protected IntMap<Entity> entities;
    protected Quaternion quaternion;
    protected BoundingBox boundingBox;
    protected boolean isTemplate;

    public Entity(Vector3 pos, int hp, int health, int types, int effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities, boolean isTemplate){
        this.instance = instance;
        this.pos = pos;
        this.hp = hp;
        this.types = types;
        this.health = health;
        this.effects = effects;
        animation = new AnimationController(instance);
        this.instance.transform.setTranslation(pos);
        this.shape = shape;
        body = new btCollisionObject();
        body.setCollisionShape(shape);
        body.setWorldTransform(instance.transform);
        this.world = world;
        tempVector = new Vector3();
        this.entities = entities;
        quaternion = new Quaternion();
        if(this instanceof Enemy || this instanceof Projectile)
            body.setCollisionFlags(body.getCollisionFlags());
        int index = 1;
        if(!isTemplate) {
            while (entities.containsKey(index))
                index++;
            entities.put(index, this);
            body.setUserValue(index);
            world.addCollisionObject(body);
            boundingBox = instance.calculateBoundingBox(new BoundingBox());
        }
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
        instance.transform.setToTranslation(pos);
        body.setWorldTransform(instance.transform);
        if(!animation.paused)
            animation.update(delta);
    }

    public void destroy() {
        world.removeCollisionObject(body);
        if(getEntry() != null)
            entities.remove(getEntry().key);
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

    public abstract void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer);

    @Override
    public void dispose() {
        shape.dispose();
    }

    public Vector3 getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": (" + pos.toString() + ", " + health + ", " + effects + ")";
    }
}
