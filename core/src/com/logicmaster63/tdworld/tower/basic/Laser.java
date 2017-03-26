package com.logicmaster63.tdworld.tower.basic;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.logicmaster63.tdworld.tower.Tower;

public class Laser extends Tower {

    public Laser(Vector3 pos, int hp, int coolDown, int types, ModelInstance instance) {
        super(pos, hp, coolDown, types, instance);
    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public void render(float delta, ModelBatch modelBatch) {
        super.render(delta, modelBatch);

    }
}
