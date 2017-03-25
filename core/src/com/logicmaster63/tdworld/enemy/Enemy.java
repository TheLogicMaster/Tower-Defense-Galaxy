package com.logicmaster63.tdworld.enemy;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.logicmaster63.tdworld.tools.MotionState;

public abstract class Enemy {

    private Vector3 position;
    private double speeed;
    private MotionState motion;
    private ModelInstance instance;

    public Enemy(Vector3 position, double speeed, ModelInstance instance, MotionState motion) {
        this.position = position;
        this.speeed = speeed;
        this.motion = motion;
        this.instance = instance;
        this.instance.transform.setTranslation(position);
    }

    public abstract void tick(float delta);

    public void render(float delta, ModelBatch modelBatch) {
        modelBatch.render(instance);
    }
}
