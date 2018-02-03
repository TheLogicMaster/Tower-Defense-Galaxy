package com.logicmaster63.tdgalaxy.controls;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class ControlHandler implements ControllerListener {

    private List<ControlListener> listeners;
    private Array<Controller> players, controllers;
    private int neededControllers = 1;
    private boolean paused = false;
    private Controller keyboard; //Place holder with null controller for purpose of specifying controls

    public ControlHandler() {
        players = new Array<Controller>(false, 4);
        players.addAll(null, null, null, null);
        controllers = new Array<Controller>();
        listeners = new ArrayList<ControlListener>();
        Controllers.addListener(this);
        if (Gdx.app.getType().equals(Application.ApplicationType.Desktop)) {
            keyboard = new TDKeyboard();
            controllers.add(keyboard);
            players.set(0, keyboard);
        }
    }

    public Controller getController(int player) {
        return players.get(player);
    }

    public boolean isPaused() {
        return paused;
    }

    public int getNeededControllers() {
        return neededControllers;
    }

    public void setNeededControllers(int neededControllers) {
        this.neededControllers = neededControllers;
        if (neededControllers > getPlayerCount())
            pause();
        else if(neededControllers <= getPlayerCount())
            unPause();
        players.setSize(neededControllers);
    }

    public Array<Controller> getPlayers() {
        return players;
    }

    public Array<Controller> getControllers() {
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

    public int getPlayerCount() {
        int count = 0;
        for (Controller controller : players)
            if (controller != null)
                count++;
        return count;
    }

    @Override
    public void connected(Controller controller) {
        System.out.println("Connected: " + controller);
        controllers.add(controller);
        if (paused) {
            for (int i = 0; i < players.size; i++) {
                if (players.get(i) == null) {
                    players.set(i, controller);
                    break;
                }
            }
            if (getPlayerCount() >= neededControllers)
                unPause();
        }
    }

    @Override
    public void disconnected(Controller controller) {
        for (Controller c : controllers)
            if (c != null && c.equals(controller))
                controllers.set(controllers.indexOf(c, true), null);
        controllers.removeValue(null, true);
        Array.ArrayIterator<Controller> iterator = new Array.ArrayIterator<Controller>(players);
        while (iterator.hasNext()) {
            Controller c = iterator.next();
            if (c != null && c == controller) {
                players.set(players.indexOf(c, true), null);
                if (neededControllers > getPlayerCount())
                    pause();
            }
        }
    }

    private void unPause() {
        paused = false;
        for (ControlListener listener : listeners)
            if (listener.onResume())
                break;
    }

    private void pause() {
        paused = true;
        for (ControlListener listener : listeners)
            if (listener.onPause())
                break;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (paused) {
            boolean contains = false;
            for (Controller player : players)
                if (player != null && player == controller)
                    contains = true;
            if (!contains) {
                System.out.println(controller);
                for(int i = 0; i < players.size; i++)
                    if(players.get(i) == null) {
                        players.set(i, controller);
                        break;
                    }
            }
            if (getPlayerCount() >= neededControllers)
                unPause();
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