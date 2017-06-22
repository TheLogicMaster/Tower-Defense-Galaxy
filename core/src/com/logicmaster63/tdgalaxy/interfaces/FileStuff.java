package com.logicmaster63.tdgalaxy.interfaces;

import com.badlogic.gdx.assets.AssetManager;

import java.util.Set;

public interface FileStuff {

    Set<Class<?>> getClasses(String packageName);

    AssetManager getExternalAssets();
}
