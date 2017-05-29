package com.logicmaster63.tdworld.ui;

public interface KeyboardHandler extends Element {

    boolean keyDown(int keycode);

    boolean keyUp(int keycode);

    boolean keyTyped(char character);
}
