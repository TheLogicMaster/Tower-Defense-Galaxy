package com.logicmaster63.tdworld.enemy;

import com.badlogic.gdx.math.Vector3;

public class EnemyData {

    private Vector3 size;
    private String name;
    private String desc;

    public EnemyData(Vector3 size, String name, String desc) {
        this.size = size;
        this.name = name;
        this.desc = desc;
    }

    public EnemyData(Vector3 size, String name) {
        this(size, name, "This enemy lacks a description, but it is surely great.");
    }

    public EnemyData(String name, String desc) {
        this(null, name, desc);
    }

    public EnemyData(String name) {
        this(null, name, "This enemy lacks a description, but it is surely great.");
    }
}
