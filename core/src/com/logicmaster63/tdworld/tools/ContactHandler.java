package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;

import java.util.ArrayList;

public class ContactHandler extends ContactListener {

    private ArrayList<Integer> ids;

    public ContactHandler(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    @Override
    public boolean onContactAdded (btManifoldPoint cp, btCollisionObjectWrapper colObj0Wrap, int partId0, int index0, btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
        System.out.println(colObj0Wrap.getCollisionObject().getUserValue());
        colObj1Wrap.getCollisionObject().getUserValue();
        return true;
    }
}
