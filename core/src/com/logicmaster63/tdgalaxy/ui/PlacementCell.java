package com.logicmaster63.tdgalaxy.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.logicmaster63.tdgalaxy.entity.Template;

import java.lang.reflect.Constructor;

public class PlacementCell {

    public Texture texture;
    public Template template;
    public Model model;
    public int price;

    public PlacementCell(Texture texture, Model model, Template template, int price) {
        this.texture = texture;
        this.template = template;
        this.model = model;
        this.price = price;
    }

    @Override
    public String toString() {
        return texture.toString() + " : " + model.toString() + " : " + template.toString() + " : " + price;
    }
}
