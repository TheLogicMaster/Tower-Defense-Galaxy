package com.logicmaster63.tdworld.enemy.boss;


import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.tools.MotionState;

public abstract class Boss extends Enemy{

    public Boss(Vector3 position, double speeed, ModelInstance instance, MotionState motion) {
        super(position, speeed, instance, motion);
    }
}
