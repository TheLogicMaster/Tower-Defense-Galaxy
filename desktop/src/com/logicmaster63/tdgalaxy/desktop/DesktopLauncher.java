package com.logicmaster63.tdgalaxy.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.google.common.reflect.ClassPath;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.Constants;
import com.logicmaster63.tdgalaxy.interfaces.FileStuff;
import com.logicmaster63.tdgalaxy.interfaces.Debug;
import com.logicmaster63.tdgalaxy.interfaces.OnlineServices;
import com.logicmaster63.tdgalaxy.interfaces.ValueReturner;
import com.logicmaster63.tdgalaxy.tools.ArchiveFileHandleResolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipFile;

public class DesktopLauncher implements FileStuff, Debug, OnlineServices {

	private JFrame frame;
	private JDialog dialog;
	private Map<String, Object> values;
	private Map<String, JButton> jButtons;
	private Map<String, TextButton> textButtons;
	private ArrayList<JLabel> labels;

	private DesktopLauncher() {
		frame = new JFrame();
		dialog = new JDialog(frame, "Dialog Example", true);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tower Defense Galaxy";

		//config.width = 1280;
		//config.height = 800;
		new LwjglApplication(new TDGalaxy(this, this, this), config);
	}

	@Override
	public void addButton(String name, final Runnable run) {
		if(jButtons == null)
			return;
		JButton button = new JButton(name);
		button.setHorizontalAlignment(SwingConstants.RIGHT);
		button.setVerticalAlignment(SwingConstants.TOP);
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
		button.setHorizontalTextPosition(SwingConstants.RIGHT);
		button.setVerticalAlignment(SwingConstants.TOP);
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
		int initSize = labels.size();
		if(labels.size() > upValues.size())
			for(int i = 0; i < initSize - upValues.size(); i++) {
				dialog.remove(labels.get(labels.size()));
				labels.remove(labels.get(labels.size()));
			}
		if(labels.size() < upValues.size())
			for(int i = 0; i < upValues.size() - initSize; i++) {
				JLabel label = new JLabel("");
				label.setHorizontalAlignment(SwingConstants.LEFT);
				label.setVerticalAlignment(SwingConstants.BOTTOM);
				labels.add(label);
				dialog.add(label);
			}
		for(int i = 0; i < upValues.entrySet().size(); i++)
			labels.get(i).setText(upValues.keySet().toArray()[i] + ": " + upValues.entrySet().toArray()[i].toString());
	}

	@Override
	public void create() {
		values = new HashMap<String, Object>();
		jButtons = new HashMap<String, JButton>();
		textButtons = new HashMap<String, TextButton>();
		labels = new ArrayList<JLabel>();

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
				dialog.setSize(500,800);
				dialog.setVisible(true);
				dialog.addWindowListener(new WindowAdapter()
				{
					@Override
					public void windowClosing(WindowEvent e)
					{
						e.getWindow().dispose();
					}
				});
			}
		});
	}

	@Override
	public void saveGame(String name) {

	}

	@Override
	public AssetManager getExternalAssets() {
		AssetManager externalAssets = null;
		try {
			ZipFile archive = new ZipFile(Gdx.files.internal("OBB.zip").file());
			ArchiveFileHandleResolver resolver = new ArchiveFileHandleResolver(archive);
			externalAssets = new AssetManager(resolver);
		} catch (IOException e) {
			Gdx.app.error("Get external assets", e.toString());
		}
		return externalAssets;
	}

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

	@Override
	public void signIn() {

	}

	@Override
	public void signOut() {

	}

	@Override
	public void rateGame() {

	}

	@Override
	public boolean isSignedIn() {
		return false;
	}

	@Override
	public void unlockAchievement(String name) {

	}

	@Override
	public void showAchievements() {

	}

	@Override
	public void resetAchievement(String name) {

	}

	@Override
	public void showScore(String name) {

	}

	@Override
	public void submitScore(String name, long highScore) {

	}

	public static void main (String[] arg) {
		new DesktopLauncher();
	}
}