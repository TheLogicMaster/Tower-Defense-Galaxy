package com.logicmaster63.tdgalaxy.map.world;

import com.badlogic.gdx.graphics.Texture;

public class CampaignWorld {

    public Texture icon;
    public String desc, name;

    public CampaignWorld(Texture icon, String name, String desc) {
        this.icon = icon;
        this.desc = desc;
        this.name = name;
    }
}
