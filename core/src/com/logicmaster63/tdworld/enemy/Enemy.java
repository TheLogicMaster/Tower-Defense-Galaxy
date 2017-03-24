package com.logicmaster63.tdworld.enemy;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.logicmaster63.tdworld.tools.MotionState;

public abstract class Enemy {

    private double x, y, speeed;
    private MotionState motionState;
    private ModelInstance modelInstance;

    public Enemy(double x, double y, double speeed, ModelInstance instance, MotionState motion) {
        this.x = x;
        this.y = y;
        this.speeed = speeed;
        this.motionState = motion;
        this.modelInstance = instance;
    }

    public abstract void tick(float delta);

    public void render(float delta, ModelBatch modelBatch) {

    }
}
