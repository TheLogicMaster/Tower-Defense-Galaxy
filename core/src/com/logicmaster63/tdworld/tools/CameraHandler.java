package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;
import com.logicmaster63.tdworld.TDWorld;

import java.util.HashMap;
import java.util.Map;

public class CameraHandler extends InputAdapter implements InputProcessor{

    private PerspectiveCamera cam;
    private Vector3 tmp;
    private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();
    private IntIntMap keys = new IntIntMap();

    class TouchInfo {
        float touchX = 0;
        float touchY = 0;
        boolean touched = false;
    }

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
        for(int i = 0; i < 5; i++){
            touches.put(i, new TouchInfo());
        }
        cam.position.set(pos);
        cam.lookAt(looking);
        cam.near = near;
        cam.far = far;
        cam.update();
        this.cam = cam;
        tmp = new Vector3(0, 0,0 );
    }

    public PerspectiveCamera getCam() {
        return cam;
    }

    public void update(float delta) {
        if (keys.containsKey(Input.Keys.Q)) {
            tmp.set(cam.direction).nor().scl(delta * 10f);
            cam.position.add(tmp);
        }
        if (keys.containsKey(Input.Keys.E)) {
            tmp.set(cam.direction).nor().scl(delta * -10f);
            cam.position.add(tmp);
        }
        cam.update();
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        float deltaX = -Gdx.input.getDeltaX() * TDWorld.sensitivity;
        float deltaY = -Gdx.input.getDeltaY() * TDWorld.sensitivity;
        cam.direction.rotate(cam.up, deltaX);
        tmp.set(cam.direction).crs(cam.up).nor();
        cam.direction.rotate(tmp, deltaY);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        keys.put(keycode, keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.remove(keycode, 0);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        tmp.set(cam.direction).nor().scl(amount * -4f);
        cam.position.add(tmp);
        return false;
    }
}
