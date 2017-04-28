package com.logicmaster63.tdworld.object;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.enums.TargetMode;
import com.logicmaster63.tdworld.screens.GameScreen;
import com.logicmaster63.tdworld.tools.Tools;
import com.logicmaster63.tdworld.tower.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AttackingObject extends Object{

    protected float coolDown, coolTime = 0, range;
    protected TargetMode targetMode = TargetMode.FIRSTEST;
    protected String attackAnimation;
    protected Vector3 attackOffset, centerOffset;
    private Vector3 rayFrom = new Vector3(), rayTo = new Vector3();
    private ClosestRayResultCallback callback;

    public AttackingObject(Vector3 pos, int hp, int health, int range, int types, int effects, float coolDown, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, Map<Integer, Object> objects, String attackAnimation, Vector3 attackOffset, boolean isTemplate){
        super(pos, hp, health, types, effects, instance, shape, world, objects, isTemplate);
        this.coolDown = coolDown;
        this.range = range;
        this.attackAnimation = attackAnimation;
        this.attackOffset = attackOffset;
        callback = new ClosestRayResultCallback(rayFrom, rayTo);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        coolTime += delta;
        if(coolTime > coolDown && canAttack()) {
            ArrayList<Object> targets = target(pos, range, objects, TargetMode.CLOSEST, this instanceof Enemy ? Tower.class: Enemy.class);
            if(targets != null) {
                attack(targets);
                coolTime = 0;
            }
        }
    }

    public void attack(ArrayList<Object> targets) {
        //if(this instanceof Basic)
            //System.out.println(this.toString() + " ---> " + target.toString());
    }

    protected btCollisionObject rayTest(Ray ray, float range) {
        rayFrom.set(ray.origin);
        rayTo.set(ray.direction).scl(range).add(rayFrom);
        GameScreen.debugVector0 = rayFrom;
        GameScreen.debugVector1 = rayTo;
        callback.setCollisionObject(null);
        callback.setClosestHitFraction(1f);
        callback.setRayToWorld(rayTo);
        callback.setRayFromWorld(rayFrom);
        world.rayTest(rayFrom, rayTo, callback);
        //if(callback.getCollisionObject() != null && this instanceof Tower)
            //System.out.println(callback.getCollisionObject().getUserValue());

        if (callback.hasHit())
            return callback.getCollisionObject();
        return null;
    }

    protected boolean canPathTo(Object target) {
        btCollisionObject object = rayTest(new Ray(pos, new Vector3(target.body.getWorldTransform().getTranslation(tempVector)).sub(pos)), range);
        //if(this instanceof Tower)
            //System.out.println(object != null && (object.getUserValue() == target.body.getUserValue()));
        return (object != null && (object.getUserValue() == target.body.getUserValue()));
    }

    public void setTargetMode(TargetMode targetMode) {
        this.targetMode = targetMode;
    }

    public boolean canAttack() {
        return true;
    }

    public ArrayList<Object> target(Vector3 pos, double range, Map<Integer, Object> objectMap, TargetMode mode, Class targetClass) {
        List<Object> objects = new ArrayList<Object>(objectMap.values());
        ArrayList<Object> array = new ArrayList<Object>();
        tempObject = null;
        if(objects.size() < 1)
            return null;
        for(int i = 0; i < objects.size(); i++) {
            tempVector.set(objects.get(i).pos);
            //if(this instanceof Basic)
            //System.out.println(i + ": " + (Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2)));
            if(tempObject == null) {
                if(targetClass.isInstance(objects.get(i)) && Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2) < Math.pow(range, 2) && canPathTo(objects.get(i)))
                    if(mode == TargetMode.GROUP)
                        array.add(objects.get(i));
                    else
                        tempObject = objects.get(i);
            } else {
                if(targetClass.isInstance(objects.get(i)) && Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2) < Math.pow(range, 2) && canPathTo(objects.get(i))) {
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
                        case GROUP:
                            array.add(objects.get(i));
                            break;
                    }
                }
            }
        }
        if(mode == TargetMode.GROUP) {
            return array;
        } else {
            if(tempObject == null)
                return null;
            array.add(tempObject);
            return array;
        }
    }
}
