package com.logicmaster63.tdworld.tools;

public class Asset {

    private Class clazz;
    private String path;

    public Asset(String path, Class clazz) {
        this.clazz = clazz;
        this.path = path;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getPath() {
        return path;
    }
}
