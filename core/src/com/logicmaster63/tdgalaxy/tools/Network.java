package com.logicmaster63.tdgalaxy.tools;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.logicmaster63.tdgalaxy.constants.ClientType;

public class Network {

    public static final int PORT = 2832;

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        //kryo.register(UpdateObjects.class);
        kryo.register(Register.class);
        kryo.register(Request.class);
        kryo.register(ChangeValue.class);
    }

    /*public class UpdateObjects {

        public ArrayList<Entity> entities;
        public Save save;

        public UpdateObjects(ArrayList<Entity> entities, Save save) {
            this.entities = entities;
            this.save = save;
        }
    }*/

    public static class Request {
        public String request;
    }

    public static class ChangeValue {

        public String value;
        public Object object;

        public ChangeValue(String value, Object object) {
            this.value = value;
            this.object = object;
        }
    }

    public static class Register {

        public ClientType type;
        public String id;

        public Register(String id, ClientType type) {
            this.id = id;
            this.type = type;
        }
    }
}
