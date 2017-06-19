package com.logicmaster63.tdgalaxy.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;

import java.lang.reflect.Constructor;

public class PlacementCell {

    public Texture texture;
    public Constructor constructor;
    public Model model;

    public PlacementCell(Texture texture, Model model, Constructor constructor) {
        this.texture = texture;
        this.constructor = constructor;
        this.model = model;
    }

    @Override
    public String toString() {
        return texture.toString() + " : " + model.toString() + " : " + constructor.toString();
    }
}
