package com.logicmaster63.tdworld.ui.window;

import com.badlogic.gdx.graphics.Texture;
import com.logicmaster63.tdworld.ui.Closeable;
import com.logicmaster63.tdworld.ui.Element;

import java.util.List;

public class DraggableWindow extends InteractableWindow{

    public DraggableWindow(Texture texture, float x, float y, float width, float height, List<Element> elements, Closeable closeable) {
        super(texture, x, y, width, height, elements, closeable);
    }

    public DraggableWindow(Texture texture, float x, float y, float width, float height, List<Element> elements) {
        this(texture, x, y, width, height, elements, null);
    }
}
