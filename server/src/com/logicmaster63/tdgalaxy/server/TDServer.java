package com.logicmaster63.tdgalaxy.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.logicmaster63.tdgalaxy.networking.Networking;
import com.logicmaster63.tdgalaxy.networking.packets.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TDServer {

    Server server;
    Map<String, Session> sessions;

    public TDServer() throws IOException {
        sessions = new HashMap<String, Session>();
        server = new Server() {
            //@Override
            //protected Connection newConnection() {
            //    return new TDConnection();
            //}
        };
        Networking.register(server);
        /*server.addListener(new Listener() {
            @Override
            public void received(Connection c, Object o) {
                //TDConnection connection = ((TDConnection) c);

                if(o instanceof RegisterClient) {
                    //connection.id = ((RegisterClient)o).id;
                    server.sendToTCP(c.getID(), new RegisterServer());
                }

                if(o instanceof CreateShare) {
                    String id = getFreeId();
                    sessions.put(id, new Session(c));
                    c.sendTCP(new ConfirmSession(id));
                }

                if(o instanceof ViewShare ) {
                    if(sessions.containsKey(((ViewShare) o).session))
                        sessions.get(((ViewShare) o).session).client = c;
                }

                if(o instanceof EntityPacket) {
                    if(sessions.get(((EntityPacket) o).session).client != null)
                        sessions.get(((EntityPacket) o).session).client.sendTCP(o);
                }
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
        });*/
        server.bind(Networking.PORT);
        server.start();
    }

    private String getFreeId() {
        String session = "";
        while ("".equals(session) || sessions.containsKey(session))
            session = String.format("%06d", (int)(Math.random() % 999999));
        return "696969";//session;
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