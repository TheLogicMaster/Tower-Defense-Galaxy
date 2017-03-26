package com.logicmaster63.tdworld.tower;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.logicmaster63.tdworld.tools.Object;

public abstract class Tower extends Object{

    private ModelInstance instance;

    public Tower(Vector3 pos, int hp, int coolDown, int types, ModelInstance instance) {
        this(pos, hp, hp, coolDown, types, instance, 0);
    }

    public Tower(Vector3 pos, int hp, int coolDown, int types, ModelInstance instance, int effects) {
        this(pos, hp, hp, coolDown, types, instance, effects);
    }

    public Tower(Vector3 pos, int hp, int health, int coolDown, int types, ModelInstance instance, int effects) {
        this.pos = pos;
        this.instance = instance;
        this.instance.transform.setTranslation(pos);
        this.health = health;
        this.hp = hp;
        this.coolDown = coolDown;
        this.effects = effects;
        this.types = types;
    }

    public abstract void tick(float delta);

    public void render(float delta, ModelBatch modelBatch) {
        //for(int i = 0; i < instance.nodes.size; i++)
            //Gdx.app.log(Double.toString(position.x), instance.nodes.get(i).id);
        //instance.nodes.get(0).rotation.set(Vector3.Y, (instance.nodes.get(0).rotation.getAngle() + 1) % 360);
        //instance.nodes.get(0).inheritTransform = false;
        //Gdx.app.log(Double.toString(position.x), Float.toString(instance.nodes.get(0).rotation.getAngle()));
        //instance.transform.rotate(Vector3.X, 1);
        modelBatch.render(instance);
    }

    @Override
    public void dispose() {

    }
}
