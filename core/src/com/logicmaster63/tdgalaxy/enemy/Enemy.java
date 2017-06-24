package com.logicmaster63.tdgalaxy.enemy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.entity.AttackingEntity;
import com.logicmaster63.tdgalaxy.entity.Entity;

import java.util.*;

public abstract class Enemy extends AttackingEntity {

    private List<Vector3> path;
    private double speeed, dist = 0;
    private int moveIndex = 0;

    public Enemy(Matrix4 transform, double speeed, int hp, int health, int range, float coolDown, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities, String attackAnimation, Vector3 attackOffset, List<Vector3> path, Map<String, Sound> sounds) {
        super(transform, hp, health, range, types, effects, coolDown, instance, shape, world, entities, attackAnimation, attackOffset, sounds);
        this.path = path;
        this.speeed = speeed;
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        if(moveIndex + 1 < path.size()) {
            transform.getTranslation(tempVector2);
            tempVector.set(path.get(moveIndex + 1));
            tempVector.sub(tempVector2).setLength((float) speeed / 10f);
            transform.translate(tempVector);
            //pos.add(tempVector);
            if(tempVector2.dst(path.get(moveIndex + 1)) < speeed / 10f) {
                transform.setToTranslation(path.get(moveIndex + 1));
                //pos.set(path.get(moveIndex + 1));
                moveIndex++;
            }
        } else {
            //Reached end
        }
    }

    @Override
    public void attack(ArrayList<Entity> target) {
        super.attack(target);
        if(attackAnimation != null)
            animation.animate(attackAnimation, 1, new AnimationController.AnimationListener() {
                @Override
                public void onEnd(AnimationController.AnimationDesc animationDesc) {
                //animation.animate("Spider_Armature|walk_ani_vor", 0);
                }

                @Override
                public void onLoop(AnimationController.AnimationDesc animationDesc) {

                }
            }, 0);
    }

    @Override
    public boolean canAttack() {
        return super.canAttack();
    }

    public ModelInstance getModelInstance() {
        return instance;
    }
    
    public double getSpeeed() {
        return speeed;
    }

    public double getDist() {
        return dist;
    }
}
