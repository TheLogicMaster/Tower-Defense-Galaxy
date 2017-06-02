package com.logicmaster63.tdworld.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.common.reflect.ClassPath;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.tools.ClassGetter;
import com.logicmaster63.tdworld.tools.CreateDebug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

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
		}, new CreateDebug() {
			JDialog d;
			java.util.List values;

			@Override
			public void update(List values) {

			}

			boolean running = true;
			@Override
			public void create() {
				values = new ArrayList();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new Thread(new Runnable() {
							@Override
							public void run() {
								while(true) {
									System.out.println(d);
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										Gdx.app.error("Run", e.toString());
									}
									if(!d.isVisible())
										break;
								}
							}
						}).start();
						JFrame frame = new JFrame();
						d = new JDialog(frame, "Dialog Example", true);
						d.setLayout( new FlowLayout() );
						JButton b = new JButton ("OK");
						b.addActionListener (new ActionListener()
						{
							public void actionPerformed(ActionEvent e )
							{
								//d.setVisible(false);
							}
						});
						d.add( new JLabel ("Click button to continue."));
						d.add(b);
						d.setSize(300,300);
						d.setVisible(true);
						d.addWindowListener(new WindowAdapter()
						{
							@Override
							public void windowClosing(WindowEvent e)
							{
								running = false;
								e.getWindow().dispose();
							}
						});
					}
				});
			}
		}), config);
	}
}
