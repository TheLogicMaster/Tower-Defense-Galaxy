package com.logicmaster63.tdworld.ui;

public abstract class Window {

    private float x, y, width, height;

    public Window(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render() {

    }
}
