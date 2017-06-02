package com.logicmaster63.tdworld.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.common.reflect.ClassPath;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.tools.ClassGetter;
import com.logicmaster63.tdworld.tools.Debug;
import com.logicmaster63.tdworld.tools.ValueReturner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

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
		}, new Debug() {
			JFrame frame = new JFrame();
			JDialog dialog = new JDialog(frame, "Dialog Example", true);
			Map<String, Object> values;
			Map<String, JButton> jButtons;
			Map<String, TextButton> textButtons;

			@Override
			public void addButton(String name, final Runnable run) {
				if(jButtons == null)
					return;
				JButton button = new JButton(name);
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						run.run();
					}
				});
				dialog.add(button);
				jButtons.put(name, button);
			}

			@Override
			public <T> void addTextButton(final String name, final ValueReturner<T> valueReturner) {
				if(jButtons == null)
					return;
				final TextField textField = new TextField();
				JButton button = new JButton(name);
				button.setHorizontalTextPosition(SwingConstants.LEFT);
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							valueReturner.value((T)(textButtons.get(name).field.getText()));
						} catch (ClassCastException ex) {
							Gdx.app.error("TextButton", "Can't cast value");
						}
					}
				});
				dialog.add(textField);
				dialog.add(button);
				textButtons.put(name, new TextButton(button, textField));
			}

			@Override
			public void removeTextButton(String name) {
				dialog.remove(textButtons.get(name).button);
				dialog.remove(textButtons.get(name).field);
				textButtons.remove(name);
			}

			@Override
			public void removeButton(String name) {
				dialog.remove(jButtons.get(name));
				jButtons.remove(name);
			}

			@Override
			public void update(Map<String, Object> upValues) {
				if(values == null)
					return;

			}

			boolean running = true;
			@Override
			public void create() {
				values = new HashMap<String, Object>();
				jButtons = new HashMap<String, JButton>();
				textButtons = new HashMap<String, TextButton>();

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						new Thread(new Runnable() {
							@Override
							public void run() {
								while(true) {
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										Gdx.app.error("Run", e.toString());
									}
									if(!dialog.isVisible())
										break;
								}
							}
						}).start();
						dialog.setLayout( new FlowLayout() );
						//dialog.add( new JLabel ("Click button to continue."));
						dialog.setSize(300,300);
						dialog.setVisible(true);
						dialog.addWindowListener(new WindowAdapter()
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

class TextButton {

	public JButton button;
	public TextField field;

	public TextButton(JButton button, TextField field) {
		this.button = button;
		this.field = field;
	}
}