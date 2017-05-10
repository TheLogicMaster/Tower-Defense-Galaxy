package com.logicmaster63.tdworld.map;

import com.badlogic.gdx.math.Vector3;

public abstract class Region {

    protected Vector3 pos;

    public Region(Vector3 pos) {
        this.pos = pos;
    }

    public abstract boolean test(float x, float y, float z);

    public boolean test(Vector3 pos) {
        return test(pos.x, pos.y, pos.z);
    }
}
