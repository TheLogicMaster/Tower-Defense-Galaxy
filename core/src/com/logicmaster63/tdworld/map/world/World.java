package com.logicmaster63.tdworld.map.world;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;

import java.util.List;

public class World {

    private List<com.logicmaster63.tdworld.map.region.Region> regions;
    private List<btCollisionObject> objects;

    public World(List<com.logicmaster63.tdworld.map.region.Region> regions, btCollisionWorld world) {
        this.regions = regions;
        for(com.logicmaster63.tdworld.map.region.Region region: regions)
            region.createCollisionObject();
    }
}
