package com.logicmaster63.tdgalaxy.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.logicmaster63.tdgalaxy.constants.ClientType;
import com.logicmaster63.tdgalaxy.networking.packets.RegisterClient;
import com.logicmaster63.tdgalaxy.networking.packets.RegisterServer;

public class Networking {

    public static final int PORT = 6969;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(RegisterClient.class);
        kryo.register(RegisterServer.class);
        kryo.register(ClientType.class);
    }
}
