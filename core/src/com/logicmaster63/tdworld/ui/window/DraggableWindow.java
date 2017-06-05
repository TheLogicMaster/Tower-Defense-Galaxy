package com.logicmaster63.tdworld.ui.window;

import com.badlogic.gdx.graphics.Texture;
import com.logicmaster63.tdworld.ui.CloseListener;
import com.logicmaster63.tdworld.ui.Element;

import java.util.List;

public class DraggableWindow extends Window{

    public DraggableWindow(Texture texture, float x, float y, float width, float height, List<Element> elements, CloseListener closeListener) {
        super(texture, x, y, width, height, elements, closeListener);
    }

    public DraggableWindow(Texture texture, float x, float y, float width, float height, List<Element> elements) {
        this(texture, x, y, width, height, elements, null);
    }


}
