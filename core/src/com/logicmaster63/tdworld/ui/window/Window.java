package com.logicmaster63.tdworld.ui.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.logicmaster63.tdworld.ui.Closeable;
import com.logicmaster63.tdworld.ui.Element;

import java.util.List;

public abstract class Window implements Element {

    protected float x, y, width, height;
    private Texture texture;
    protected Vector3 tmp, tmp2;
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
        tmp = new Vector3();
        tmp2 = new Vector3();
    }

    public Window(Texture texture, float x, float y, float width, float height, List<Element> elements) {
        this(texture, x, y, width, height, elements, null);
    }

    public void render(SpriteBatch spriteBatch, Camera camera) {
        camera.unproject(tmp.set(x, y, 0));
        camera.unproject(tmp2.set(width, y + height, 0));
        spriteBatch.draw(texture, tmp.x, tmp2.y, tmp2.x, tmp.y - tmp2.y);
    }

    //Possibly take out <------------------------------------------
    public void resize(int width, int height) {

    }

    public void close() {
        if(closeable != null)
            closeable.close();
        elements.remove(this);
    }
}
