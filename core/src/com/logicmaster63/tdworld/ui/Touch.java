package com.logicmaster63.tdworld.ui;

public class Touch {

    public float x, y;
    public int id;

    public Touch() {

    }

    public Touch(float x, float y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public Touch set(float x, float y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
        return this;
    }
}
