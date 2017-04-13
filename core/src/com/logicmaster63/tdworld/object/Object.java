package com.logicmaster63.tdworld.object;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.Disposable;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.enums.TargetMode;
import com.logicmaster63.tdworld.projectiles.Projectile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Object implements Disposable{

    protected Vector3 pos, tempVector;
    protected int hp, health, effects, types;
    protected AnimationController animation;
    protected ModelInstance instance;
    protected btCollisionShape shape;
    protected btCollisionWorld world;
    protected btCollisionObject body;
    protected Object tempObject;
    protected Map<Integer, Object> objects;
    protected Quaternion quaternion;
    protected BoundingBox boundingBox;


    public Object(Vector3 pos, int hp, int health, int types, int effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, Map<Integer, Object> objects){
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
        if(this instanceof Enemy || this instanceof Projectile)
            body.setCollisionFlags(body.getCollisionFlags());
        int index = 1;
        while(objects.containsKey(index))
            index++;
        objects.put(index, this);
        System.out.println(index);
        body.setUserValue(index);
        world.addCollisionObject(body);
        this.world = world;
        tempVector = new Vector3();
        this.objects = objects;
        quaternion = new Quaternion();
        boundingBox = instance.calculateBoundingBox(new BoundingBox());
        for(Node node: instance.nodes)
            System.out.println();
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
    }

    public Object target(Vector3 pos, double range, Map<Integer, Object> objectMap, TargetMode mode) {
        List<Object> objects = new ArrayList<Object>(objectMap.values());
        tempObject = null;
        if(objects.size() < 1)
            return null;
        for(int i = 1; i < objects.size(); i++) {
            tempVector.set(objects.get(i).pos);
            if(tempObject == null) {
                if(Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2) < Math.pow(range, 2))
                    tempObject = objects.get(i);
            } else {
                if(Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2) < Math.pow(range, 2)) {
                    switch (mode) {
                        case CLOSEST: //Consider using with another test vector object: pos.sub(tempVector).len2() < pos.sub(tempObject.pos).len2()
                            if(Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2) < Math.pow(pos.x - tempObject.pos.x, 2) + Math.pow(pos.y - tempObject.pos.y, 2) + Math.pow(pos.z - tempObject.pos.z, 2))
                                tempObject = objects.get(i);
                            break;
                        case FURTHEST:
                            if(Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2) > Math.pow(pos.x - tempObject.pos.x, 2) + Math.pow(pos.y - tempObject.pos.y, 2) + Math.pow(pos.z - tempObject.pos.z, 2))
                                tempObject = objects.get(i);
                            break;
                        case FASTEST:
                            if (objects.get(i) instanceof Enemy && ((Enemy) objects.get(i)).getSpeeed() > ((Enemy) tempObject).getSpeeed())
                                tempObject = objects.get(i);
                            break;
                        case SLOWEST:
                            if (objects.get(i) instanceof Enemy && ((Enemy) objects.get(i)).getSpeeed() < ((Enemy) tempObject).getSpeeed())
                                tempObject = objects.get(i);
                            break;
                        case STRONGEST:
                            if (objects.get(i).health > tempObject.health)
                                tempObject = objects.get(i);
                            break;
                        case WEAKEST:
                            if (objects.get(i).health < tempObject.health)
                                tempObject = objects.get(i);
                            break;
                        case FIRSTEST:
                            if(objects.get(i) instanceof Enemy && ((Enemy) objects.get(i)).getDist() > ((Enemy) tempObject).getDist())
                                tempObject = objects.get(i);
                            break;
                        case LASTEST:
                            if(objects.get(i) instanceof Enemy && ((Enemy) objects.get(i)).getDist() < ((Enemy) tempObject).getDist())
                                tempObject = objects.get(i);
                            break;
                    }
                }
            }
        }
        return tempObject;
    }

    public void destroy() {
        world.removeCollisionObject(body);
        objects.remove(getEntry().getKey());
        dispose();
    }

    private Map.Entry<Integer, Object> getEntry() {
        for(Map.Entry<Integer, Object> entry: objects.entrySet())
            if(entry.equals(this))
                return entry;
        return null;
    }

    public abstract void render(float delta, ModelBatch modelBatch);

    @Override
    public void dispose() {
        shape.dispose();
    }

    public Vector3 getPos() {
        return pos;
    }
}
