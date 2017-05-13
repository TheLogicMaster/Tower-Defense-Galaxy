package com.logicmaster63.tdworld;

import android.content.Context;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.logicmaster63.tdworld.tools.ClassGetter;
import dalvik.system.DexFile;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;
		initialize(new TDWorld(new ClassGetter() {
			@Override
			public Set<Class<?>> getClasses(String packageName) {
				try {
					Set<Class<?>> classes = new HashSet<Class<?>>();
					DexFile dex = new DexFile(getContext().getApplicationInfo().sourceDir);
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
					Enumeration<String> entries = dex.entries();
					while (entries.hasMoreElements()) {
						String entry = entries.nextElement();
						if (entry.toLowerCase().startsWith(packageName.toLowerCase()))
							classes.add(classLoader.loadClass(entry));
					}
					return classes;
				} catch (Exception e) {
					Gdx.app.error("Errorororor", e.toString());
				}
				return null;
			}
		}), config);
	}
}
