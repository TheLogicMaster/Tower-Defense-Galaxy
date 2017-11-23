package com.logicmaster63.tdgalaxy.interfaces;

import java.util.Map;

public interface Debug {

    void createWindow(String id);

    void disposeWindow(String id);

    void updateValues(String id, Map<String, Object> values);

    void updateValue(String id, String name, Object value);

    void removeValue(String id, String name);

    void addButton(String name, Runnable run);

    <T> void addTextButton(String name, ValueReturner<T> valueReturner);

    void removeButton(String name);

    void removeTextButton(String name);
}
