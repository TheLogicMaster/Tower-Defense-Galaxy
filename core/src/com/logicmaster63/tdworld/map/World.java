package com.logicmaster63.tdworld.map;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;

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
