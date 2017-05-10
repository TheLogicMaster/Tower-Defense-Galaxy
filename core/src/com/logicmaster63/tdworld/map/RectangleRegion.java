package com.logicmaster63.tdworld.map;

import com.badlogic.gdx.math.Vector3;

public class RectangleRegion extends Region {

    private float width, height, depth;

    public RectangleRegion(Vector3 pos, float width, float height, float depth) {
        super(pos);
        this.height = height;
        this.width = width;
        this.depth = depth;
    }

    public RectangleRegion(float x, float y, float z, float width, float height, float depth) {
        this(new Vector3(x, y, z), width, height, depth);
    }

    @Override
    public boolean test(float x, float y, float z) {
        return x > pos.x - width / 2 && x < pos.x + width / 2 && y > pos.y - height / 2 && y < pos.y + height / 2 && z > pos.z - depth / 2 && z < pos.z + depth / 2;
    }
}
