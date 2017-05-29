package com.logicmaster63.tdworld.ui.window;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.logicmaster63.tdworld.ui.Closeable;
import com.logicmaster63.tdworld.ui.Element;

import java.util.List;

public abstract class Window implements Element {

    protected float x, y, width, height;
    private Texture texture;
    private Closeable closeable;
    private List<Element> elements;

    public Window(Texture texture, float x, float y, float width, float height, List<Element> elements, Closeable closeable) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.closeable = closeable;
        this.elements = elements;
    }

    public Window(Texture texture, float x, float y, float width, float height, List<Element> elements) {
        this(texture, x, y, width, height, elements, null);
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, x, y, width, height);
    }

    public void close() {
        if(closeable != null)
            closeable.close();
        elements.remove(this);
    }
}
