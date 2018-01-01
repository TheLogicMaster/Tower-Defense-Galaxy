package com.logicmaster63.tdgalaxy.networking;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.networking.packets.RegisterClient;
import com.logicmaster63.tdgalaxy.networking.packets.RegisterServer;

import java.io.IOException;

public class TDClient extends Listener {

    protected Client client;

    public TDClient() {
        client = new Client();
        client.start();
        Networking.register(client);
        client.addListener(this);
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public void connect(final int timeout, final String host, final int port) {
        new Thread("Connect") {
            public void run () {
                try {
                    client.connect(timeout, host, port);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public void reconnect() {
        try {
            client.reconnect();
        } catch (IOException e) {
            Gdx.app.error("TDClient", e.toString());
        }
    }

    public void sendTCP(Object o) {
        client.sendTCP(o);
    }

    @Override
    public void connected(Connection connection) {
        client.sendTCP(new RegisterClient().set(TDGalaxy.preferences.getId()));
    }

    @Override
    public void disconnected(Connection connection) {

    }

    @Override
    public void received(Connection connection, Object o) {
        if(o instanceof RegisterServer)
            Gdx.app.log("TDClient", "Connected to server");
    }

    @Override
    public void idle(Connection connection) {

    }

    public void addListener(Listener listener) {
        client.addListener(listener);
    }
}
