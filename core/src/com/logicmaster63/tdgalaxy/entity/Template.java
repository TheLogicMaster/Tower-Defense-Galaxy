package com.logicmaster63.tdgalaxy.entity;

import com.badlogic.gdx.Gdx;
import com.logicmaster63.tdgalaxy.tools.Tools;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class Template<T extends Entity>{

    protected Constructor constructor;
    protected Object[] constants;

    public Template(Constructor constructor, Object... constants) {
        this.constructor = constructor;
        this.constants = constants;
    }

    @SuppressWarnings("unchecked")
    public T create(Object... args) {
        try {
            return (T)(constructor.newInstance(Tools.concatenate(args, constants)));
        } catch (Exception e) {
            Gdx.app.error("Template", e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return constructor.toString();
    }
}
