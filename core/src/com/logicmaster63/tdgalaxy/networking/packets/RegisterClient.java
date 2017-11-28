package com.logicmaster63.tdgalaxy.networking.packets;

import com.logicmaster63.tdgalaxy.constants.ClientType;

public class RegisterClient {

    public String id;

    public RegisterClient set(String id) {
        this.id = id;
        return this;
    }
}
