package com.logicmaster63.tdworld.ui.window;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.logicmaster63.tdworld.ui.CloseListener;
import com.logicmaster63.tdworld.ui.Element;
import com.logicmaster63.tdworld.ui.MouseHandler;

import java.util.List;

public abstract class InteractableWindow extends Window implements MouseHandler {

    public InteractableWindow(Texture texture, float x, float y, float width, float height, List<Element> elements, CloseListener closeListener) {
        super(texture, x, y, width, height, elements, closeListener);
    }

    public InteractableWindow(Texture texture, float x, float y, float width, float height, List<Element> elements) {
        this(texture, x, y, width, height, elements, null);
    }

    @Override
    public void render(SpriteBatch spriteBatch, Camera camera) {
        super.render(spriteBatch, camera);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean onWindow(float x, float y) {
        return this.x < x && x < this.x + width && this.y < y && y < this.y + height;
    }
}
