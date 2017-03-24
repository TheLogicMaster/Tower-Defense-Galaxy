package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class MotionState extends btMotionState {

    Matrix4 transform;

    public MotionState () {

    }

    public MotionState (Matrix4 transform) {
        this.transform = transform;
    }

    @Override
    public void getWorldTransform (Matrix4 worldTrans) {
        worldTrans.set(transform);
    }

    @Override
    public void setWorldTransform (Matrix4 worldTrans) {
        transform.set(worldTrans);
    }
}