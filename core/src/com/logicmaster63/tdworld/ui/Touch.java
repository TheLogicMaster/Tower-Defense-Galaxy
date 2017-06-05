package com.logicmaster63.tdworld.ui;

public class Touch {

    public float x, y;
    public int id, button;

    public Touch() {

    }

    public Touch(float x, float y, int id, int button) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.button = button;
    }

    public Touch set(float x, float y, int id, int button) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.button = button;
        return this;
    }
}
