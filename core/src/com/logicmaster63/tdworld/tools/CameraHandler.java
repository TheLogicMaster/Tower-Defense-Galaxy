package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;
import com.logicmaster63.tdworld.TDWorld;

public class CameraHandler{

    private PerspectiveCamera cam;
    private Vector3 tmp, origin;
    private float xRot, yRot, zRot;

    public CameraHandler(Vector3 pos, float near, float far) {
        this(new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), pos, new Vector3(0, 0, 0), near, far);
    }

    public CameraHandler(Vector3 pos, int width, int height, float near, float far) {
        this(new PerspectiveCamera(67, width, height), pos, new Vector3(0, 0, 0), near, far);
    }

    public CameraHandler(Vector3 pos, int fov, int width, int height, float near, float far) {
        this(new PerspectiveCamera(fov, width, height), pos, new Vector3(0, 0, 0), near, far);
    }

    public CameraHandler(Vector3 pos, Vector3 looking, int fov, int width, int height, float near, float far) {
        this(new PerspectiveCamera(fov, width, height), pos, looking, near, far);
    }

    public CameraHandler(PerspectiveCamera cam, Vector3 pos, Vector3 looking, float near, float far) {
        cam.position.set(pos);
        cam.lookAt(looking);
        cam.near = near;
        cam.far = far;
        cam.update();
        this.cam = cam;
        tmp = new Vector3(0, 0,0 );
        origin = new Vector3(0, 0, 0);
    }

    public PerspectiveCamera getCam() {
        return cam;
    }

    public void update(float delta, IntIntMap keys) {
        xRot = getCameraRotationX() + 180;
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
            rotate(dir, delta * 100f);
        cam.update();
    }

    public void touchDragged (int screenX, int screenY, int pointer) {
        float deltaX = -Gdx.input.getDeltaX() * TDWorld.getSensitivity();
        float deltaY = -Gdx.input.getDeltaY() * TDWorld.getSensitivity();
        //cam.direction.rotate(cam.up, deltaX);
        //tempVector.set(cam.direction).crs(cam.up).nor();
        //cam.direction.rotate(tempVector, deltaY);
        cam.rotateAround(origin, Vector3.Z, deltaX);
        cam.rotateAround(origin, Vector3.X, deltaY);
    }

    public void scrolled(int amount) {
        tmp.set(cam.direction).nor().scl(amount * -4f);
        cam.position.add(tmp);
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
        if((dir & 1 << 0) == 1 << 0 || (dir & 1 << 1) == 1 << 1)
            cam.rotateAround(origin, Vector3.X, angle * angleXMult);
        if((dir & 1 << 2) == 1 << 2 || (dir & 1 << 3) == 1 << 3)
            cam.rotateAround(origin, Vector3.Z, angle * angleZMult);
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
