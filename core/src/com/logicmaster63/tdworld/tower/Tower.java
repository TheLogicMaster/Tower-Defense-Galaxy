package com.logicmaster63.tdworld.tower;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.enums.TargetMode;
import com.logicmaster63.tdworld.object.AttackingObject;
import com.logicmaster63.tdworld.object.Object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Tower extends AttackingObject{

    public Tower(Vector3 pos, int hp, int range, float coolDown, int types, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, Map<Integer, Object> objects, String attackAnimation, Vector3 attackOffset, boolean isTemplate) {
        this(pos, hp, hp, range, coolDown, types, instance, shape, 0, world, objects, attackAnimation, attackOffset, isTemplate);
    }

    public Tower(Vector3 pos, int hp, int health, int range, float coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, Map<Integer, Object> objects, String attackAnimation, Vector3 attackOffset, boolean isTemplate) {
        super(pos, hp, health, range, types, effects, coolDown, instance, shape, world, objects, attackAnimation, attackOffset, isTemplate);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }

    @Override
    public void attack(ArrayList<Object> target) {
        super.attack(target);
        if(!"".equals(attackAnimation))
            animation.animate(attackAnimation, 1, new AnimationController.AnimationListener() {
                @Override
                public void onEnd(AnimationController.AnimationDesc animationDesc) {
                    animation.animate("Spider_Armature|walk_ani_vor", 0);
                }

                @Override
                public void onLoop(AnimationController.AnimationDesc animationDesc) {

                }
            }, 0);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer) {
        //for(int i = 0; i < instance.nodes.size; i++)
            //Gdx.app.log(Double.toString(position.x), instance.nodes.get(i).id);
        //instance.nodes.get(0).rotation.set(Vector3.Y, (instance.nodes.get(0).rotation.getAngle() + 1) % 360);
        //instance.nodes.get(0).inheritTransform = false;
        //Gdx.app.log(Double.toString(position.x), Float.toString(instance.nodes.get(0).rotation.getAngle()));
        //instance.transform.rotate(Vector3.X, 1);
        modelBatch.render(instance);
    }

    @Override
    public boolean canAttack() {
        return super.canAttack();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
