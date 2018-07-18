package com.logicmaster63.tdgalaxy.enemy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.entity.Attackerer;
import com.logicmaster63.tdgalaxy.entity.AttackingEntity;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.tools.Effects;
import com.logicmaster63.tdgalaxy.tools.Types;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public abstract class AttackingEnemy extends Enemy implements AttackingEntity {

    Attackerer attackerer;
    String attackAnimation;

    public AttackingEnemy(Matrix4 transform, double speeed, int hp, int health, int range, float coolDown, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities, String attackAnimation, Vector3 attackOffset, List<Vector3> path, Map<String, Sound> sounds) {
        super(transform, speeed, hp, health, types, effects, instance, shape, world, entities, path, sounds);
        attackerer = new Attackerer(this, transform, range, coolDown, world, entities, attackAnimation, attackOffset);
        this.attackAnimation = attackAnimation;
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        attackerer.tick(delta);
    }

    @Override
    public void attack(ArrayList<Entity> target) {
        attackerer.attack(target);
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
        return attackerer.canAttack();
    }
}
