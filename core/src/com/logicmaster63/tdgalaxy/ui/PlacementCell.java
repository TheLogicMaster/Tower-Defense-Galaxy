package com.logicmaster63.tdgalaxy.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.logicmaster63.tdgalaxy.entity.EntityTemplate;

public class PlacementCell {

    public Texture texture;
    public EntityTemplate entityTemplate;
    public Model model;
    public int price;

    public PlacementCell(Texture texture, Model model, EntityTemplate entityTemplate, int price) {
        this.texture = texture;
        this.entityTemplate = entityTemplate;
        this.model = model;
        this.price = price;
    }

    @Override
    public String toString() {
        return texture.toString() + " : " + model.toString() + " : " + entityTemplate.toString() + " : " + price;
    }
}
