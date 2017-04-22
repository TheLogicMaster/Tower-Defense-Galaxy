package com.logicmaster63.tdworld.object;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.enemy.basic.Basic;
import com.logicmaster63.tdworld.enums.TargetMode;
import com.logicmaster63.tdworld.tower.Tower;

import java.util.Map;

public abstract class AttackingObject extends Object{

    protected float coolDown, coolTime = 0, range;
    protected TargetMode targetMode = TargetMode.FIRSTEST;
    protected String attackAnimation;

    public AttackingObject(Vector3 pos, int hp, int health, int range, int types, int effects, float coolDown, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, Map<Integer, Object> objects, String attackAnimation){
        super(pos, hp, health, types, effects, instance, shape, world, objects);
        this.coolDown = coolDown;
        this.range = range;
        this.attackAnimation = attackAnimation;
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        coolTime += delta;
        if(coolTime > coolDown && canAttack()) {
            Object target = target(pos, range, objects, TargetMode.CLOSEST, this instanceof Enemy ? Tower.class: Enemy.class);
            if(target != null) {
                attack(target);
                coolTime = 0;
            }
        }
    }

    public void attack(Object target) {
        //if(this instanceof Basic)
            //System.out.println(this.toString() + " ---> " + target.toString());
    }

    public void setTargetMode(TargetMode targetMode) {
        this.targetMode = targetMode;
    }

    public boolean canAttack() {
        return true;
    }
}
