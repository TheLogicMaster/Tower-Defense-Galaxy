package com.logicmaster63.tdworld.enemy;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.logicmaster63.tdworld.tools.MotionState;

public class Basic extends Enemy{

    public Basic(Vector3 position, double speeed, ModelInstance modelInstance, MotionState motionState) {
        super(position, speeed, modelInstance, motionState);
    }

    @Override
    public void tick(float delta) {

    }
}
