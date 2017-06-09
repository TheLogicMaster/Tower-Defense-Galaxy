package com.logicmaster63.tdgalaxy.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.logicmaster63.tdgalaxy.enums.ClientType;
import com.logicmaster63.tdgalaxy.tools.Network;

import java.io.IOException;
import java.util.ArrayList;

public class TDServer {

    Server server;

    public TDServer() throws IOException {
        server = new Server() {
            @Override
            protected Connection newConnection() {
                return new TDConnection();
            }
        };
        Network.register(server);
        server.addListener(new Listener() {

            @Override
            public void received(Connection c, Object o) {
                TDConnection connection = ((TDConnection) c);

                if(o instanceof Network.Request) {
                    if("ShareHosts".equals(((Network.Request) o).request)) {
                        ArrayList<String> ids = new ArrayList<String>();
                        for(int i = 0; i < server.getConnections().length; i++)
                            if(((TDConnection) server.getConnections()[i]).type == ClientType.SHARE_HOST)
                                ids.add(((TDConnection) server.getConnections()[i]).id);
                        Network.ChangeValue changeValue = new Network.ChangeValue("ShareHosts", ids);
                        server.sendToTCP(c.getID(), changeValue);
                    }
                }
            }
        });
        server.bind(Network.PORT);
        server.start();
    }

    public static void main (String[] args) throws IOException {
        Log.set(Log.LEVEL_DEBUG);
        new TDServer();
    }

    public class TDConnection extends Connection {
        public String id;
        public ClientType type;
    }
}