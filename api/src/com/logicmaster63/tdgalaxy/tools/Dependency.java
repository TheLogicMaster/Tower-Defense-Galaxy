package com.logicmaster63.tdgalaxy.tools;

public class Dependency {

    private String name;
    private Class clazz;

    public Dependency(Class clazz, String name) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class getClazz() {
        return clazz;
    }
}
