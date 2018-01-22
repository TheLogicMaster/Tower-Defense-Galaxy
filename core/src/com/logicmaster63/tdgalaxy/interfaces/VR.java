package com.logicmaster63.tdgalaxy.interfaces;

import com.badlogic.gdx.graphics.Camera;
import com.logicmaster63.tdgalaxy.constants.Eye;

public interface VR {

    void initialize(int width, int height, int viewportWidth, int viewportHeight);

    boolean isInitialized();

    void update(float delta);

    void startRender();

    void endRender();

    Camera beginCamera(Eye eye);

    void endCamera(Eye eye);

    void close();
}
