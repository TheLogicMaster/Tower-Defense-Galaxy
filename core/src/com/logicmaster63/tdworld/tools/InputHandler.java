package com.logicmaster63.tdworld.tools;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntIntMap;

import java.util.HashMap;
import java.util.Map;

public class InputHandler implements InputProcessor {

    private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();
    private CameraHandler cam;
    private IntIntMap keys = new IntIntMap();

    public InputHandler(CameraHandler cam) {
        this.cam = cam;
        for(int i = 0; i < 5; i++){
            touches.put(i, new TouchInfo());
        }
    }

    public void update(float delta) {
        cam.update(delta, keys);
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        cam.touchDragged(screenX, screenY, pointer);
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
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        cam.scrolled(amount);
        return false;
    }
}
