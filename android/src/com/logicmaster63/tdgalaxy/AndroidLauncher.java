package com.logicmaster63.tdgalaxy;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.logicmaster63.tdgalaxy.interfaces.FileStuff;
import dalvik.system.DexFile;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;
		config.useWakelock = true;
		config.useImmersiveMode = false;
		config.useGyroscope = true;
		initialize(new TDWorld(new FileStuff() {
			@Override
			public Set<Class<?>> getClasses(String packageName) {
				Set<Class<?>> classes = new HashSet<Class<?>>();
				try {
					DexFile dex = new DexFile(getContext().getApplicationInfo().sourceDir);
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
					Enumeration<String> entries = dex.entries();
					while (entries.hasMoreElements()) {
						String entry = entries.nextElement();
						if (entry.toLowerCase().startsWith(packageName.toLowerCase()))
							classes.add(classLoader.loadClass(entry));
					}
				} catch (Exception e) {
					Gdx.app.error("Errorororor", e.toString());
				}
				return classes;
			}
		}, null), config);
	}
}
