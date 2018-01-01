package com.logicmaster63.tdgalaxy.server;

import com.esotericsoftware.kryonet.Connection;

public class Session {

    public Session(Connection host) {
        this.host = host;
    }

    Connection host;
    Connection client;
}
