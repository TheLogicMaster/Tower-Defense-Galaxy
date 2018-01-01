package com.logicmaster63.tdgalaxy.networking.packets;

import java.util.List;

public class EntityPacket {

    public List<EntityRender> entities;
    public String session;

    public EntityPacket(List<EntityRender> entities, String session) {
        this.entities = entities;
        this.session = session;
    }

    public EntityPacket() {

    }

    public class EntityRender {

    }
}