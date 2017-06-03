package com.logicmaster63.tdworld.tools;

import com.google.common.collect.BiMap;

import java.util.Map;

public interface Debug {

    void create();

    void update(Map<String, Object> values);

    void addButton(String name, Runnable run);

    <T> void addTextButton(String name, ValueReturner<T> valueReturner);

    void removeButton(String name);

    void removeTextButton(String name);
}
