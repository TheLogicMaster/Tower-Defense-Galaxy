package com.logicmaster63.tdworld.ui;

public class TouchInfo {

    public float touchX = 0;
    public float touchY = 0;
    public boolean touched = false;

    public TouchInfo() {

    }

    public TouchInfo(float touchX, float touchY, boolean touched) {
        this.touchX = touchX;
        this.touchY = touchY;
        this.touched = touched;
    }
}
