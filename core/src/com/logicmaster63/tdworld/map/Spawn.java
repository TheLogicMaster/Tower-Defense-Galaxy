package com.logicmaster63.tdworld.map;

public class Spawn {

    private String name;
    private int delay;

    public Spawn(String name, int delay) {
        this.name = name;
        this.delay = delay;
    }

    public String getName() {
        return name;
    }

    public int getDelay() {
        return delay;
    }
}
