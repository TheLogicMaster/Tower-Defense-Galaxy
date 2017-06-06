package com.logicmaster63.tdworld.ui.window;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.ui.CloseListener;
import com.logicmaster63.tdworld.ui.Element;
import com.logicmaster63.tdworld.ui.Touch;
import com.logicmaster63.tdworld.ui.TouchInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopupWindow extends InteractableWindow {

    private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();
    private Touch lastTouch;

    public PopupWindow(Texture texture, float x, float y, float width, float height, List<Element> elements, CloseListener closeListener) {
        super(texture, x, y, width, height, elements, closeListener);
        for(int i = 0; i < 5; i++){
            touches.put(i, new TouchInfo());
        }
        lastTouch = new Touch();
    }

    public PopupWindow(Texture texture, float x, float y, float width, float height, List<Element> elements) {
        this(texture, x, y, width, height, elements, null);
    }

    @Override
    public void render(SpriteBatch spriteBatch, Camera camera) {
        camera.unproject(tmp.set(x + 10, y + 20, 0));
        super.render(spriteBatch, camera);
        TDWorld.getFonts().get("ui32").draw(spriteBatch, "This is a window", tmp.x, tmp.y);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
        }
        lastTouch.set(screenX, screenY, pointer);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(touches(touches) == 1 && lastTouch.id == pointer)
            close();
        if(pointer < 5){
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
        }
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
