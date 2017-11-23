package com.logicmaster63.tdgalaxy.tools;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brummid.vrcamera.RendererForVR;
import com.brummid.vrcamera.VRCamera;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.CameraType;

public class CameraHandler implements InputProcessor {

    private PerspectiveCamera cam;
    private Vector3 tmp, pivotPoint;
    private float xRot, yRot, zRot;
    private RendererForVR rendererForVR;
    private TDGalaxy game;
    private CameraType controlType;

    public CameraHandler(Vector3 pos, TDGalaxy game, float near, float far, RendererForVR rendererForVR, Viewport viewport) {
        this(new PerspectiveCamera(67, viewport.getWorldWidth(), viewport.getWorldHeight()), null /* new VRCamera(67, near, far, 0.5f, viewport.getWorldWidth(), viewport.getWorldHeight(), rendererForVR) */, pos, game, new Vector3(0, 0, 0), near, far, rendererForVR);
    }

    public CameraHandler(Vector3 pos, TDGalaxy game, int width, int height, float near, float far, RendererForVR rendererForVR) {
        this(new PerspectiveCamera(67, width, height), new VRCamera(67, near, far, 0.5f, (float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight(), rendererForVR), pos, game, new Vector3(0, 0, 0), near, far, rendererForVR);
    }

    public CameraHandler(PerspectiveCamera cam, VRCamera vrCamera, Vector3 pos, TDGalaxy game, Vector3 looking, float near, float far, RendererForVR rendererForVR) {
        cam.position.set(pos);
        cam.lookAt(looking);
        cam.near = near;
        cam.far = far;
        this.game = game;
        //cam.update();
        this.rendererForVR = rendererForVR;
        this.cam = cam;
        if(vrCamera != null) {
            vrCamera.setToTranslation(pos);
            vrCamera.lookAt(looking);
        }
        tmp = new Vector3(0, 0,0 );
        pivotPoint = new Vector3(0, 0, 0);
    }

    public PerspectiveCamera getCam() {
        return cam;
    }

    public void render(Batch batch) {
        if(TDGalaxy.preferences.isVr())
            ;//vrCamera.render(batch);
        else {
            rendererForVR.renderForVR(cam);
        }
    }

    public void update(float delta) {
        if(TDGalaxy.preferences.isVr())
            ;
        else
            cam.update();
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

    public CameraType getControlType() {
        return controlType;
    }

    public void setControlType(CameraType controlType) {
        this.controlType = controlType;
    }
}
