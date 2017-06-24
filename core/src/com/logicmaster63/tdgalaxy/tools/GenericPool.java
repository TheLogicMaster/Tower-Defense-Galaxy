package com.logicmaster63.tdgalaxy.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GenericPool<T> extends Pool<T> {

    private Constructor<T> constructor;
    private Object[] args;
    
    public GenericPool(Constructor<T> constructor, int inititalCapacity, int maxCapacity, Object... args) {
        super(inititalCapacity, maxCapacity);
        this.constructor = constructor;
        this.args = args;
    }

    @Override
    protected T newObject() {
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            Gdx.app.error("Generic Pool", e.toString());
        }
        return null;
    }
}
