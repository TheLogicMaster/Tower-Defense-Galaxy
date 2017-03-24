package com.logicmaster63.tdworld.tower;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public abstract class Tower {

    private int x, y;
    private ModelInstance instance;

    public Tower(int x, int y, ModelInstance instance) {
        this.x = x;
        this.y = y;
        this.instance = instance;
    }

    public abstract void tick(float delta);

    public void render(float delta, ModelBatch modelBatch) {
        modelBatch.render(instance);
    }
}
