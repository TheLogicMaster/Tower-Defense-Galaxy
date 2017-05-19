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
    protected Map<Integer, Entity> objects;
    protected Quaternion quaternion;
    protected BoundingBox boundingBox;
    protected boolean isTemplate;

    public Entity(Vector3 pos, int hp, int health, int types, int effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, Map<Integer, Entity> objects, boolean isTemplate){
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
        this.objects = objects;
        quaternion = new Quaternion();
        if(this instanceof Enemy || this instanceof Projectile)
            body.setCollisionFlags(body.getCollisionFlags());
        int index = 1;
        if(!isTemplate) {
            //System.out.println(getClass().getSimpleName());
            while (objects.containsKey(index))
                index++;
            objects.put(index, this);
            //System.out.println(index);
            body.setUserValue(index);
            //System.out.println("Index: " + index);
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
        if(getEntry() != null && getEntry().getKey() != null)
            objects.remove(getEntry().getKey());
        dispose();
    }

    protected int getNextIndex() {
        int index = 1;
        while(objects.containsKey(index))
            index++;
        return index;
    }

    protected Map.Entry<Integer, Entity> getEntry() {
        for(Map.Entry<Integer, Entity> entry: objects.entrySet())
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
