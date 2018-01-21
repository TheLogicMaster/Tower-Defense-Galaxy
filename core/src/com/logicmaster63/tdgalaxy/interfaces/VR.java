package com.logicmaster63.tdgalaxy.interfaces;

import com.badlogic.gdx.graphics.Camera;
import com.logicmaster63.tdgalaxy.constants.Eye;

public interface VR {

    void initialize();

    boolean isInitialized();

    void update();

    void startRender();

    void endRender();

    Camera beginCamera(Eye eye);

    void endCamera(Eye eye);

    void close();
}
