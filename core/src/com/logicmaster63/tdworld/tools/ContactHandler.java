package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.entity.Entity;
import com.logicmaster63.tdworld.projectiles.Projectile;
import com.logicmaster63.tdworld.tower.Tower;
import com.logicmaster63.tdworld.traps.Trap;

import java.util.Map;

public class ContactHandler extends ContactListener {

    private Map<Integer, Entity> objects;
    private Vector3 tempVector;
    private ModelInstance planet;

    public ContactHandler(Map<Integer, Entity> objects, ModelInstance planet) {
        this.objects = objects;
        tempVector = new Vector3();
        this.planet = planet;
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean onContactAdded (btManifoldPoint cp, btCollisionObjectWrapper colObj0Wrap, int partId0, int index0, btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
        Entity entity0, entity1;
        if(colObj1Wrap.getCollisionObject().getUserValue() == 0)
            entity1 = null;
        else
            entity1 = objects.get(colObj1Wrap.getCollisionObject().getUserValue());
        entity0 = objects.get(colObj0Wrap.getCollisionObject().getUserValue());
        if(entity0 instanceof Trap) {
            if(entity1 instanceof Enemy)
                ((Trap) entity0).collision(entity1);
        }
        if(entity0 instanceof Projectile) {
            if(entity1 == null || entity1 instanceof Enemy || entity1 instanceof Tower)
                ((Projectile) entity0).collision(entity1);
        }
        return true;
    }
}
