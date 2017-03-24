package com.logicmaster63.tdworld.enemy;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.logicmaster63.tdworld.tools.MotionState;

public class Basic extends Enemy{

    public Basic(double x, double y, double speeed, ModelInstance modelInstance, MotionState motionState) {
        super(x, y, speeed, modelInstance, motionState);
    }

    @Override
    public void tick(float delta) {

    }
}
