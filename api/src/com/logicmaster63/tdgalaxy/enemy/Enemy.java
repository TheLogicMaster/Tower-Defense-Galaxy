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
import com.logicmaster63.tdgalaxy.tools.Effects;
import com.logicmaster63.tdgalaxy.tools.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;

import java.util.*;

public abstract class Enemy extends Entity {

    private List<Vector3> path;
    private double speeed, dist = 0;
    private int moveIndex = 0;

    public Enemy(Matrix4 transform, double speeed, int hp, int health, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities, List<Vector3> path, Map<String, Sound> sounds) {
        super(transform, hp, health, types, effects, instance, shape, world, entities, sounds);
        this.path = path;
        this.speeed = speeed;
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        if(moveIndex + 1 < path.size()) {
            transform.getTranslation(tempVector2);
            tempVector.set(path.get(moveIndex + 1));
            tempVector.sub(tempVector2).setLength((float) speeed * 10f * delta);
            transform.translate(tempVector);
            //pos.add(tempVector);
            if(tempVector2.dst(path.get(moveIndex + 1)) < speeed * 10f * delta) {
                transform.setToTranslation(path.get(moveIndex + 1));
                //pos.set(path.get(moveIndex + 1));
                moveIndex++;
            }
        } else {
            //Reached end
        }
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
