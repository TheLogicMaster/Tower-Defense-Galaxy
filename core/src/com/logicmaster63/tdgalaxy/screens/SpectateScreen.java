package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.networking.packets.EntityPacket;
import com.logicmaster63.tdgalaxy.networking.packets.ViewShare;

import java.util.HashMap;
import java.util.Map;

public class SpectateScreen extends TDScreen {

    private Map<Integer, EntityPacket.EntityRender> entities;

    public SpectateScreen(TDGalaxy game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        entities = new HashMap<Integer, EntityPacket.EntityRender>();

        game.getClient().sendTCP(new ViewShare("696969"));

        game.getClient().addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                if(o instanceof EntityPacket)
                    Gdx.app.log("Entities", "Session" + ((EntityPacket) o).session);
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
