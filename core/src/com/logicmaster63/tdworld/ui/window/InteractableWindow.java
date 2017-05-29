package com.logicmaster63.tdworld.ui.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.logicmaster63.tdworld.ui.Closeable;
import com.logicmaster63.tdworld.ui.Element;
import com.logicmaster63.tdworld.ui.MouseHandler;

import java.util.List;

public abstract class InteractableWindow extends Window implements MouseHandler {

    public InteractableWindow(Texture texture, float x, float y, float width, float height, List<Element> elements, Closeable closeable) {
        super(texture, x, y, width, height, elements, closeable);
    }

    public InteractableWindow(Texture texture, float x, float y, float width, float height, List<Element> elements) {
        this(texture, x, y, width, height, elements, null);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean onWindow(float x, float y) {
        return this.x < x && x < this.x + width && Gdx.graphics.getHeight() - this.y < y && y < Gdx.graphics.getHeight() - this.y + height;
    }
}
