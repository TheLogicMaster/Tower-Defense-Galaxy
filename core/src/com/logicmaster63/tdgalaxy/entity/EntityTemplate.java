package com.logicmaster63.tdgalaxy.entity;

import java.lang.reflect.Constructor;

public abstract class EntityTemplate<T extends Entity>{

    protected Constructor<T> constructor;
    protected Object[] constants;

    public EntityTemplate(Constructor<T> constructor, Object... constants) {
        this.constructor = constructor;
        this.constants = constants;
    }

    public abstract T create(Object... args);

    public Object[] getConstants() {
        return constants;
    }

    @Override
    public String toString() {
        return constructor.toString();
    }
}
