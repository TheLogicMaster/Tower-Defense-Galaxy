package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.interfaces.CameraRenderer;
import com.logicmaster63.tdgalaxy.networking.Networking;
import com.logicmaster63.tdgalaxy.networking.TDClient;

public class MultiplayerScreen extends TDScreen implements CameraRenderer {

    private Texture background;

    public MultiplayerScreen(TDGalaxy game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        background = new Texture("theme/basic/ui/Window.png");
    }

    @Override
    public void renderForCamera(Camera camera) {
        spriteBatch.begin();
        //spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
    }
}