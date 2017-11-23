package com.logicmaster63.tdgalaxy.tools;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.logicmaster63.tdgalaxy.controls.TDController;
import com.logicmaster63.tdgalaxy.interfaces.ControlListener;

import java.util.Arrays;
import java.util.List;

public class ControlHandler implements ControllerListener {

    private List<ControlListener> listeners;
    private Array<TDController> controllers;
    private int neededControllers = 0;
    private boolean paused = false;

    public ControlHandler() {
        controllers = new Array<TDController>(2);
        Controllers.addListener(this);
    }

    public TDController getController(int player) {
        return controllers.get(player);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setNeededControllers(int neededControllers) {
        this.neededControllers = neededControllers;
        //if() { //if not enough controllers pause
        //
        //}
    }

    public Array<TDController> getControllers() {
        return controllers;
    }

    public void addListener(ControlListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ControlListener listener) {
        listeners.remove(listener);
    }

    public void reset() {
        listeners.clear();
        paused = false;
    }

    public int getControllerCount() {
        int count = 0;
        for(TDController controller: controllers)
            if(controller != null)
                count++;
        return count;
    }

    @Override
    public void connected(Controller controller) {
        if(paused) {
            if (controllers.get(0) == null)
                controllers.set(0, new TDController(controller, 0));
            else
                controllers.set(1, new TDController(controller, 1));
            paused = false;
            for(ControlListener listener: listeners)
                if(listener.onResume())
                    break;
        }
    }

    @Override
    public void disconnected(Controller controller) {
        for(TDController c: controllers)
            if(c != null && c.getController().equals(controller)) {
                if(neededControllers == 2 || neededControllers == 1 && c.getPlayer() == 0) {
                    paused = true;
                    for(ControlListener listener: listeners)
                        if(listener.onPause(true, neededControllers - getControllerCount()))
                            break;
                }
                controllers.set(c.getPlayer(), null);
            }
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if(paused) {
            //Set controller to p1 or p2 if isn't connected already
            paused = false;
            for(ControlListener listener: listeners)
                if(listener.onResume())
                    break;
        }
        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}