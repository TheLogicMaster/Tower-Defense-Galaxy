package com.logicmaster63.tdgalaxy.map.world;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdgalaxy.map.region.Region;

import java.util.List;

public class World {

    private List<Region> regions;
    private List<btCollisionObject> objects;

    public World(List<Region> regions, btCollisionWorld world) {
        this.regions = regions;
        for(Region region: regions)
            region.createCollisionObject();
    }
}
