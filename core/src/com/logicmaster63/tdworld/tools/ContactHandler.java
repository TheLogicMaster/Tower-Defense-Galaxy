package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBody;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.object.Object;
import com.logicmaster63.tdworld.projectiles.Projectile;
import com.logicmaster63.tdworld.tower.Tower;
import com.logicmaster63.tdworld.traps.Trap;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactHandler extends ContactListener {

    private HashMap<Integer, Object> objects;
    private Vector3 tempVector;
    private ModelInstance planet;

    public ContactHandler(HashMap<Integer, Object> objects, ModelInstance planet) {
        this.objects = objects;
        tempVector = new Vector3();
        this.planet = planet;
    }

    @Override
    public boolean onContactAdded (btManifoldPoint cp, btCollisionObjectWrapper colObj0Wrap, int partId0, int index0, btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
        Object object0, object1;
        if(colObj1Wrap.getCollisionObject().getUserValue() == 0)
            object1 = null;
        else
            object1 = objects.get(colObj1Wrap.getCollisionObject().getUserValue());
        object0 = objects.get(colObj0Wrap.getCollisionObject().getUserValue());
        if(object0 instanceof Trap) {
            if(object1 instanceof Enemy)
                ((Trap)object0).collision(object1);
        }
        if(object0 instanceof Projectile) {
            if(object1 == null || object1 instanceof Enemy || object1 instanceof Tower)
                ((Projectile)object0).collision(object1);
        }
        return true;
    }
}
