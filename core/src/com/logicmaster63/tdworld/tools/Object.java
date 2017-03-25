package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public abstract class Object implements Disposable{

    protected Vector3 pos;
    protected int hp, health, effects, coolDown, types;

}
