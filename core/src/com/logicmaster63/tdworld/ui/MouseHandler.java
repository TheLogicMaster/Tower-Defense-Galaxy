package com.logicmaster63.tdworld.ui;

public interface MouseHandler extends Element {

    boolean mouseMoved(int screenX, int screenY);

    boolean scrolled(int amount);

    boolean touchDragged (int screenX, int screenY, int pointer);

    boolean touchDown(int screenX, int screenY, int pointer, int button);

    boolean touchUp(int screenX, int screenY, int pointer, int button);

    boolean onWindow(float x, float y);

    boolean click(int screenX, int screenY, int pointer, int button);
}
