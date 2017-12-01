package com.logicmaster63.tdgalaxy.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.logicmaster63.tdgalaxy.networking.Networking;
import com.logicmaster63.tdgalaxy.networking.packets.CreateShare;
import com.logicmaster63.tdgalaxy.networking.packets.RegisterClient;
import com.logicmaster63.tdgalaxy.networking.packets.RegisterServer;
import com.logicmaster63.tdgalaxy.networking.packets.ViewShare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TDServer {

    Server server;
    List<Session> sessions;

    public TDServer() throws IOException {
        sessions = new ArrayList<Session>();
        server = new Server() {
            @Override
            protected Connection newConnection() {
                return new TDConnection();
            }
        };
        Networking.register(server);
        server.addListener(new Listener() {
            @Override
            public void received(Connection c, Object o) {
                TDConnection connection = ((TDConnection) c);

                if(o instanceof RegisterClient) {
                    connection.id = ((RegisterClient)o).id;
                    server.sendToTCP(c.getID(), new RegisterServer());
                }

                if(o instanceof CreateShare) {

                }

                if(o instanceof ViewShare ) {

                }

                /*if(o instanceof Network.Request) {
                    if("ShareHosts".equals(((Network.Request) o).request)) {
                        ArrayList<String> ids = new ArrayList<String>();
                        for(int i = 0; i < server.getConnections().length; i++)
                            if(((TDConnection) server.getConnections()[i]).type == ClientType.SHARE_HOST)
                                ids.add(((TDConnection) server.getConnections()[i]).id);
                        Network.ChangeValue changeValue = new Network.ChangeValue("ShareHosts", ids);
                        server.sendToTCP(c.getID(), changeValue);
                    }
                }*/
            }

            @Override
            public void connected(Connection connection) {

            }

            @Override
            public void disconnected(Connection connection) {

            }

            @Override
            public void idle(Connection connection) {

            }
        });
        server.bind(Networking.PORT);
        server.start();
    }

    public static void main (String[] args) throws IOException {
        System.out.println("FYHUJBVGYVUHJKLJVCTYFUGHIJKNJHCFGIJOLMKNJHGCGIHJOPKMLKNJ HGVYGUIHOJLMKN VGHJO");
        Log.set(Log.LEVEL_DEBUG);
        new TDServer();
    }

    public class TDConnection extends Connection {
        public String id;
    }
}