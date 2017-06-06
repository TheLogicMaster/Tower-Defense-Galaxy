package com.logicmaster63.tdworld.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;
import com.brummid.vrcamera.RendererForVR;
import com.brummid.vrcamera.VRCamera;
import com.logicmaster63.tdworld.TDWorld;

public class CameraHandler implements InputProcessor {

    private PerspectiveCamera cam;
    private Vector3 tmp, origin;
    private float xRot, yRot, zRot;
    private VRCamera vrCamera;
    private RendererForVR rendererForVR;

    public CameraHandler(Vector3 pos, float near, float far, RendererForVR rendererForVR) {
        this(new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), new VRCamera(67, near, far, 0.5f, (float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight(), rendererForVR), pos, new Vector3(0, 0, 0), near, far, rendererForVR);
    }

    public CameraHandler(Vector3 pos, int width, int height, float near, float far, RendererForVR rendererForVR) {
        this(new PerspectiveCamera(67, width, height), new VRCamera(67, near, far, 0.5f, (float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight(), rendererForVR), pos, new Vector3(0, 0, 0), near, far, rendererForVR);
    }

    public CameraHandler(PerspectiveCamera cam, VRCamera vrCamera, Vector3 pos, Vector3 looking, float near, float far, RendererForVR rendererForVR) {
        cam.position.set(pos);
        cam.lookAt(looking);
        cam.near = near;
        cam.far = far;
        //cam.update();
        this.rendererForVR = rendererForVR;
        this.cam = cam;
        this.vrCamera = vrCamera;
        vrCamera.setToTranslation(pos);
        vrCamera.lookAt(looking);
        tmp = new Vector3(0, 0,0 );
        origin = new Vector3(0, 0, 0);
    }

    public PerspectiveCamera getCam() {
        return cam;
    }

    public VRCamera getVRCam() {
        return vrCamera;
    }

    public void render(Batch batch) {
        if(TDWorld.isVr())
            vrCamera.render(batch);
        else {
            rendererForVR.renderForVR(cam);
        }
    }

    public void update(float delta) {
        /*xRot = getCameraRotationX() + 180;
        yRot = getCameraRotationY() + 180;
        zRot = getCameraRotationZ() + 180;
        if (keys.containsKey(Input.Keys.Q)) {
            tmp.set(cam.direction).nor().scl(delta * 100f);
            cam.position.add(tmp);
        }
        if (keys.containsKey(Input.Keys.E)) {
            tmp.set(cam.direction).nor().scl(delta * -100f);
            cam.position.add(tmp);
        }
        int dir = 0;
        if (keys.containsKey(Input.Keys.W))
            dir += 1;
        if (keys.containsKey(Input.Keys.S))
            dir += 2;
        if (keys.containsKey(Input.Keys.A))
            dir += 4;
        if (keys.containsKey(Input.Keys.D))
            dir += 8;
        if(dir > 0)
            rotate(dir, delta * 100f);*/
        cam.update();
        vrCamera.update();
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        float deltaX = -Gdx.input.getDeltaX() * TDWorld.getSensitivity();
        float deltaY = -Gdx.input.getDeltaY() * TDWorld.getSensitivity();
        //cam.direction.rotate(cam.up, deltaX);
        //tempVector.set(cam.direction).crs(cam.up).nor();
        //cam.direction.rotate(tempVector, deltaY);
        cam.rotateAround(origin, Vector3.Z, deltaX);
        cam.rotateAround(origin, Vector3.X, deltaY);

        tmp.set(origin);
        tmp.sub(vrCamera.getPosition());
        vrCamera.translate(tmp);
        vrCamera.rotate(0, 0, deltaX);
        tmp.rotate(Vector3.Z, deltaX);
        vrCamera.translate(-tmp.x, -tmp.y, -tmp.z);

        tmp.set(origin);
        tmp.sub(vrCamera.getPosition());
        vrCamera.translate(tmp);
        vrCamera.rotate(deltaY, 0, 0);
        tmp.rotate(Vector3.X, deltaY);
        vrCamera.translate(-tmp.x, -tmp.y, -tmp.z);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        tmp.set(cam.direction).nor().scl(amount * -4f);
        cam.position.add(tmp);
        return true;
    }

    private void rotate(int dir, float angle) {
        float angleXMult = 1, angleZMult = 1;
        if((dir & 1) == 1)
            angleXMult *= -1;
        if((dir & 1 << 3) == 1 << 3)
            angleZMult *= -1;
        if(xRot > 180) {
            zRot += angle;
        } else {
            zRot -= angle;
        }
        if((dir & 1 << 0) == 1 << 0 || (dir & 1 << 1) == 1 << 1) {
            cam.rotateAround(origin, Vector3.X, angle * angleXMult);
            tmp.set(origin);
            tmp.sub(vrCamera.getPosition());
            vrCamera.translate(tmp);
            vrCamera.rotate(angle * angleXMult, 0, 0);
            tmp.rotate(Vector3.X, angle * angleXMult);
            vrCamera.translate(-tmp.x, -tmp.y, -tmp.z);
        }
        if((dir & 1 << 2) == 1 << 2 || (dir & 1 << 3) == 1 << 3) {
            cam.rotateAround(origin, Vector3.Z, angle * angleZMult);
            tmp.set(origin);
            tmp.sub(vrCamera.getPosition());
            vrCamera.translate(tmp);
            vrCamera.rotate(0, 0, angle * angleZMult);
            tmp.rotate(Vector3.Z, angle * angleZMult);
            vrCamera.translate(-tmp.x, -tmp.y, -tmp.z);
        }
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

    public float getCameraRotationX() {
        return (float)Math.atan2(cam.up.z, cam.up.y) * MathUtils.radiansToDegrees;
    }

    public float getCameraRotationY() {
        return (float)Math.atan2(cam.up.x, cam.up.z) * MathUtils.radiansToDegrees;
    }

    public float getCameraRotationZ() {
        return (float)Math.atan2(cam.up.x, cam.up.y) * MathUtils.radiansToDegrees;
    }
}
