package com.logicmaster63.tdworld.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.common.reflect.ClassPath;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.tools.ClassGetter;

import java.util.HashSet;
import java.util.Set;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tower Defense World";
		//config.width = 1280;
		//config.height = 800;
		new LwjglApplication(new TDWorld(new ClassGetter() {
			@Override
			public Set<Class<?>> getClasses(String packageName) {
				final ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Set<Class<?>> classes = new HashSet<Class<?>>();
				try {
					for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
						if (info.getPackageName().startsWith(packageName))
							classes.add(Class.forName(packageName + "." + info.getSimpleName()));
					}
				} catch (Exception e) {
					Gdx.app.error("Loading Classes", e.toString());
				}
				return classes;
			}
		}), config);
	}
}
