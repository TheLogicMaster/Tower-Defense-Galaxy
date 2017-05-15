package com.logicmaster63.tdworld.tools;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.logicmaster63.tdworld.enums.ClientType;
import com.logicmaster63.tdworld.object.Object;

import java.util.ArrayList;

public class Network {

    public static final int PORT = 66666;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(RegisterClient.class);
        kryo.register(UpdateObjects.class);
    }

    public class RegisterClient {
        public String id;
        public String name;
        public ClientType type;
    }

    public class UpdateObjects {
        public ArrayList<Object> objects;

    }
}
