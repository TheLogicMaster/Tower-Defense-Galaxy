package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.logicmaster63.tdgalaxy.TDGalaxy;

public abstract class T3DScreen extends TDScreen {

    public T3DScreen(TDGalaxy game) {
        super(game);
    }

    @Override
    protected Camera createCamera() {
        Camera camera = new PerspectiveCamera(67, viewport.getWorldWidth(), viewport.getWorldHeight());
        camera.position.set(0, 500, 0);
        camera.lookAt(0, 0, 0);
        camera.near = 0.1f;
        camera.far = 10000;
        return camera;
    }
}
