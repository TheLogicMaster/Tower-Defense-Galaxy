package com.logicmaster63.tdworld.map;

import com.badlogic.gdx.math.Vector3;

public class SphereRegion extends Region {

    private float radius;


    public SphereRegion(Vector3 pos, float radius) {
        super(pos);
        this.radius = radius;
    }

    @Override
    public boolean test(float x, float y, float z) {
        return Math.pow(pos.x - x, 2) + Math.pow(pos.y - y, 2) + Math.pow(pos.z - z, 2) < Math.pow(radius, 2);
    }
}
