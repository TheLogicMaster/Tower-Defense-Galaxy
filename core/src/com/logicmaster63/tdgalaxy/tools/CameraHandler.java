package com.logicmaster63.tdgalaxy.tools;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.CameraType;

public class CameraHandler implements InputProcessor {

    private Camera cam;
    private Vector3 tmp, pivotPoint;
    private float xRot, yRot, zRot;
    private TDGalaxy game;
    private CameraType cameraType;

    public CameraHandler(Camera cam, TDGalaxy game, CameraType cameraType) {
        this.game = game;
        this.cam = cam;
        tmp = new Vector3(0, 0,0 );
        pivotPoint = new Vector3(0, 0, 0);
        this.cameraType = cameraType;

    }

    public void update(float delta) {

    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        float deltaX = -Gdx.input.getDeltaX() * TDGalaxy.preferences.getSensitivity();
        float deltaY = -Gdx.input.getDeltaY() * TDGalaxy.preferences.getSensitivity();

        cam.rotateAround(pivotPoint, Vector3.Z, deltaX);
        cam.rotateAround(pivotPoint, Vector3.X, deltaY);

        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        tmp.set(cam.direction).nor().scl(amount * -4f);
        cam.position.add(tmp);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public CameraType getCameraType() {
        return cameraType;
    }
}
