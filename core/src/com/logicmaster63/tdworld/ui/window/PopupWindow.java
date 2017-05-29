package com.logicmaster63.tdworld.ui.window;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.ui.Closeable;
import com.logicmaster63.tdworld.ui.Element;
import com.logicmaster63.tdworld.ui.TouchInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopupWindow extends InteractableWindow {

    private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();

    public PopupWindow(Texture texture, float x, float y, float width, float height, List<Element> elements, Closeable closeable) {
        super(texture, x, y, width, height, elements, closeable);
        for(int i = 0; i < 5; i++){
            touches.put(i, new TouchInfo());
        }
    }

    public PopupWindow(Texture texture, float x, float y, float width, float height, List<Element> elements) {
        this(texture, x, y, width, height, elements, null);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
        TDWorld.getFonts().get("ui32").draw(spriteBatch, "This is a window", x + 10, y + height - 10);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
        }
        //if(touches(touches) == 1)
            close();
        return true;
    }

    private int touches(Map<Integer, TouchInfo> touches) {
        int num = 0;
        for(int i = 0; i < 5; i++)
            if(touches.get(i).touched)
                num++;
        return num;
    }
}
