package com.logicmaster63.tdworld.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.logicmaster63.tdworld.TDWorld;

public class PopupWindow extends DraggableWindow {

    public PopupWindow(Texture texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
        TDWorld.getFonts().get("ui32").draw(spriteBatch, "This is a window", x + 10, y + height - 10);
    }
}
