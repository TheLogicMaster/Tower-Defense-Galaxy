package com.logicmaster63.tdgalaxy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.logicmaster63.tdgalaxy.constants.Eye;

public class VRCamera {

    public VRCamera() {
        leftCamera = new PerspectiveCamera();
        rightCamera = new PerspectiveCamera();
    }

    private PerspectiveCamera leftCamera, rightCamera;

    public void update() {

    }

    public Camera getEye(Eye eye) {
        if(eye.equals(Eye.LEFT))
            return leftCamera;
        else
            return rightCamera;
    }
}
