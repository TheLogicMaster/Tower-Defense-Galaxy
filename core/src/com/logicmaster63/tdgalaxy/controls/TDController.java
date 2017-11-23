package com.logicmaster63.tdgalaxy.controls;

import com.badlogic.gdx.controllers.Controller;

public class TDController {

    private int player; //0 is player one and so forth
    private Controller controller;

    public TDController(Controller controller, int player) {
        this.player = player;
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return player + controller.getName();
    }
}
