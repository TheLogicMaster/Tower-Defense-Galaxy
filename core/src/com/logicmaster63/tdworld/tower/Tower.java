package com.logicmaster63.tdworld.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public abstract class Tower {

    private Vector3 position;
    private ModelInstance instance;

    public Tower(Vector3 position, ModelInstance instance) {
        this.position = position;
        this.instance = instance;
        this.instance.transform.setTranslation(position);
    }

    public abstract void tick(float delta);

    public void render(float delta, ModelBatch modelBatch) {
        //for(int i = 0; i < instance.nodes.size; i++)
            //Gdx.app.log(Double.toString(position.x), instance.nodes.get(i).id);
        //instance.nodes.get(0).rotation.set(Vector3.Y, (instance.nodes.get(0).rotation.getAngle() + 1) % 360);
        //instance.nodes.get(0).inheritTransform = false;
        //Gdx.app.log(Double.toString(position.x), Float.toString(instance.nodes.get(0).rotation.getAngle()));
        instance.transform.rotate(Vector3.X, 1);
        modelBatch.render(instance);
    }
}
