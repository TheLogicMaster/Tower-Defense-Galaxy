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

public class PopupWindow extends Window {

    public PopupWindow(Texture texture, float x, float y, float width, float height, List<Element> elements, CloseListener closeListener) {
        super(texture, x, y, width, height, elements, closeListener);
    }

    public PopupWindow(Texture texture, float x, float y, float width, float height, List<Element> elements) {
        this(texture, x, y, width, height, elements, null);
    }

    @Override
    public void render(SpriteBatch spriteBatch, Camera camera) {
        super.render(spriteBatch, camera);
        camera.unproject(tmp.set(x + 10, y + 20, 0));
        TDWorld.getFonts().get("ui32").draw(spriteBatch, "This is a window", tmp.x, tmp.y);
    }

    @Override
    public boolean click(int screenX, int screenY, int pointer, int button) {
        close();
        return true;
    }
}
