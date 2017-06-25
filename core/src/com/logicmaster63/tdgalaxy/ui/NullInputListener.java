package com.logicmaster63.tdgalaxy.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class NullInputListener extends InputListener {

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {

    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

    }

    @Override
    public boolean scrolled(InputEvent event, float x, float y, int amount) {
        return true;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(InputEvent event, char character) {
        return true;
    }
}
