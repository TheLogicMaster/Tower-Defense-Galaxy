package com.logicmaster63.tdgalaxy.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

public class TDKeyboard implements Controller {

    @Override
    public boolean getButton(int buttonCode) {
        return false;
    }

    @Override
    public float getAxis(int axisCode) {
        return 0;
    }

    @Override
    public PovDirection getPov(int povCode) {
        if(Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D))
            return PovDirection.northEast;
        if(Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A))
            return PovDirection.northWest;
        if(Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D))
            return PovDirection.southEast;
        if(Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A))
            return PovDirection.southWest;
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            return PovDirection.north;
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            return PovDirection.west;
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            return PovDirection.south;
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            return PovDirection.east;
        return PovDirection.center;
    }

    @Override
    public boolean getSliderX(int sliderCode) {
        return false;
    }

    @Override
    public boolean getSliderY(int sliderCode) {
        return false;
    }

    @Override
    public Vector3 getAccelerometer(int accelerometerCode) {
        return Vector3.Zero;
    }

    @Override
    public void setAccelerometerSensitivity(float sensitivity) {

    }

    @Override
    public String getName() {
        return "Keyboard and Mouse";
    }

    @Override
    public void addListener(ControllerListener listener) {

    }

    @Override
    public void removeListener(ControllerListener listener) {

    }
}
