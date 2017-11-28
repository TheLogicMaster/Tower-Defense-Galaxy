package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.networking.Networking;
import com.logicmaster63.tdgalaxy.networking.TDClient;

public class MultiplayerScreen extends TDScreen {

    private Texture background;

    public MultiplayerScreen(TDGalaxy game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        background = new Texture("theme/basic/ui/Window.png");

        final TDClient client = new TDClient();
        client.connect(5000, "99.36.127.68", Networking.PORT);

        /*Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                client.sendMessage();
            }
        }, 5).run();*/
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        //spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();

        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
    }
}