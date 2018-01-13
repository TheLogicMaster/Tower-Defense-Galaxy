package com.logicmaster63.tdgalaxy.interfaces;

import com.badlogic.gdx.graphics.PerspectiveCamera;

public interface VR {

    void initialize();

    PerspectiveCamera getLeftCamera();

    PerspectiveCamera getRightCamera();

    void close();
}
