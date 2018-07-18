package com.logicmaster63.tdgalaxy.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class EntityProperties {

    public EntityProperties(String propertyPath) {
        //load(new JsonReader().parse(Gdx.files.internal(propertyPath)));
    }

    protected void load(JsonValue properties) {

    }
}
