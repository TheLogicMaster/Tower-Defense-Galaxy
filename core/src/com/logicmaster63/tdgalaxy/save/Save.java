package com.logicmaster63.tdgalaxy.save;

import com.logicmaster63.tdgalaxy.entity.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Save implements Serializable {

    public int money;
    public List entities;

    public Save(int money, Map<Integer, Entity> entities) {
        this.money = money;

    }
}
