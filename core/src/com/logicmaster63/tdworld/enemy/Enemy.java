package com.logicmaster63.tdworld.enemy;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.object.AttackingEntity;
import com.logicmaster63.tdworld.object.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Enemy extends AttackingEntity {

    private List<Vector3> path;
    private double speeed, dist = 0;
    private int moveIndex = 0;

    public Enemy(Vector3 pos, double speeed, int hp, int health, int range, float coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, Map<Integer, Entity> objects, String attackAnimation, Vector3 attackOffset, boolean isTemplate, List<Vector3> path) {
        super(pos, hp, health, range, types, effects, coolDown, instance, shape, world, objects, attackAnimation, attackOffset, isTemplate);
        this.path = path;
        this.speeed = speeed;
    }

    public Enemy(Vector3 position, double speeed, int hp, int range, float coolDown, int types, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, Map<Integer, Entity> objects, String attackAnimation, Vector3 attackOffset, boolean isTemplate, List<Vector3> path) {
        this(position, speeed, hp, hp, range, coolDown, types, instance, shape, 0, world, objects, attackAnimation, attackOffset, isTemplate, path);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        if(moveIndex + 1 < path.size()) {
            tempVector.set(path.get(moveIndex + 1));
            tempVector.sub(pos).setLength((float) speeed / 10f);
            pos.add(tempVector);
            if(pos.dst(path.get(moveIndex + 1)) < speeed / 10f) {
                pos.set(path.get(moveIndex + 1));
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
    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer) {
        modelBatch.render(instance);
    }

    @Override
    public boolean canAttack() {
        return super.canAttack();
    }

    public ModelInstance getModelInstance() {
        return instance;
    }

    public Vector3 getPos() {
        return pos;
    }

    public double getSpeeed() {
        return speeed;
    }

    public double getDist() {
        return dist;
    }
}
