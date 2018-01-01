package com.logicmaster63.tdgalaxy.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.logicmaster63.tdgalaxy.networking.packets.*;

public class Networking {

    public static final int PORT = 6969;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(RegisterClient.class);
        kryo.register(RegisterServer.class);
        kryo.register(EntityPacket.class);
        kryo.register(EntityPacket.EntityRender.class);
        kryo.register(CreateShare.class);
        kryo.register(ViewShare.class);
        kryo.register(ConfirmSession.class);
    }
}
