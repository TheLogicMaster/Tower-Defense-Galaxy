package com.logicmaster63.tdgalaxy.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.vr.VRContext;
import com.google.common.reflect.ClassPath;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.Eye;
import com.logicmaster63.tdgalaxy.interfaces.*;
import com.logicmaster63.tdgalaxy.tools.ArchiveFileHandleResolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.zip.ZipFile;

public class DesktopLauncher implements FileStuff, Debug, OnlineServices, VR {

	private final boolean IS_PRODUCTION = false;

	private Map<String, JDialog> windows;
	private Map<String, Map<String, Object>> values;
	private Map<String, List<JLabel>> labels;
	private Map<String, List<JButton>> buttons;
	private VRContext context;

	private DesktopLauncher(String startingMode, boolean isDebug) {
		windows = new HashMap<String, JDialog>();
		values = new HashMap<String, Map<String, Object>>();
		labels = new HashMap<String, List<JLabel>>();
		buttons = new HashMap<String, List<JButton>>();

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Tower Defense Galaxy");
		//config.setIdleFPS(60);
		//config.useVsync(false);
		//config.width = 1280;
		//config.height = 800;
		new Lwjgl3Application(new TDGalaxy(startingMode, this, this, this, this, isDebug), config);
	}

	@Override
	public boolean isInitialized() {
		return context != null;
	}

	@Override
	public void initialize(int width, int height, int viewportWidth, int viewportHeight) {
		try {
			context = new VRContext();

			context.getEyeData(VRContext.Eye.Left).camera.far = 10000;
			context.getEyeData(VRContext.Eye.Right).camera.far = 10000;
			context.getTrackerSpaceOriginToWorldSpaceTranslationOffset().set(new Vector3(250, 20, 250));

			context.addListener(new VRContext.VRDeviceListener() {
				public void connected (VRContext.VRDevice device) {
					Gdx.app.log("VR", device + " connected");
					if (device.getType() == VRContext.VRDeviceType.Controller && device.getModelInstance() != null)
						;//modelInstances.add(device.getModelInstance());
				}

				public void disconnected (VRContext.VRDevice device) {
					Gdx.app.log("VR", device + " disconnected");
					if (device.getType() == VRContext.VRDeviceType.Controller && device.getModelInstance() != null)
						;//modelInstances.removeValue(device.getModelInstance(), true);
				}

				public void buttonPressed (VRContext.VRDevice device, int button) {
					Gdx.app.log("VR", device + " button pressed: " + button);

					if (device == context.getDeviceByType(VRContext.VRDeviceType.Controller)) {
						if (button == VRContext.VRControllerButtons.SteamVR_Trigger)
							;//isTeleporting = true;
					}
				}

				public void buttonReleased (VRContext.VRDevice device, int button) {
					Gdx.app.log("VR", device + " button released: " + button);

					if (device == context.getDeviceByType(VRContext.VRDeviceType.Controller)) {
						if (button == VRContext.VRControllerButtons.SteamVR_Trigger) {

						}
					}
				}
			});
		} catch (Exception e) {
			Gdx.app.error("VR", e.toString());
		}
	}

	@Override
	public void update(float delta) {
		context.pollEvents();
	}

	@Override
	public void startRender() {
		context.begin();
	}

	@Override
	public void endRender() {
		context.renderToCompanionWindow(VRContext.Eye.Left);
		context.end();
	}

	@Override
	public Camera beginCamera(Eye eye) {
		context.beginEye(eye.equals(Eye.LEFT)? VRContext.Eye.Left: VRContext.Eye.Right);
		return context.getEyeData(eye.equals(Eye.LEFT)? VRContext.Eye.Left: VRContext.Eye.Right).camera;
	}

	@Override
	public void endCamera(Eye eye) {
		context.endEye();
	}

	@Override
	public void close() {
		if(isInitialized())
			context.dispose();
	}

	@Override
	public void addButton(final String id, final String name, final Runnable run) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JButton button = new JButton(name);
				button.setHorizontalAlignment(SwingConstants.RIGHT);
				button.setVerticalAlignment(SwingConstants.TOP);
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						run.run();
					}
				});
				System.out.println(windows.values());
				windows.get(id).add(button);
				buttons.get(id).add(button);
			}
		});
	}

	@Override
	public <T> void addTextButton(final String name, final ValueReturner<T> valueReturner) {
		/*if(jButtons == null)
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
		textButtons.put(name, new TextButton(button, textField));*/
	}

	@Override
	public void removeTextButton(String name) {
		/*dialog.remove(textButtons.get(name).button);
		dialog.remove(textButtons.get(name).field);
		textButtons.remove(name);*/
	}

	@Override
	public void removeButton(String name) {
		/*dialog.remove(jButtons.get(name));
		jButtons.remove(name);*/
	}

	private void updateLabels(String id) {
		while(labels.get(id).size() < values.get(id).size()) {
			JLabel jLabel = new JLabel();
			jLabel.setFont(new Font("Courier", Font.BOLD,50));
			labels.get(id).add(jLabel);
			windows.get(id).add(jLabel);
		}
		while(labels.get(id).size() > values.get(id).size()) {
			windows.get(id).remove(labels.get(id).get(labels.get(id).size() - 1));
			labels.get(id).remove(labels.get(id).size() - 1);
		}
		for(int i = 0; i < values.get(id).size(); i++) {
			//labels.get(id).get(i).setName((String)values.get(id).keySet().toArray()[i]);
			labels.get(id).get(i).setText(values.get(id).keySet().toArray()[i] + ": " + values.get(id).values().toArray()[i].toString());
		}
	}

	@Override
	public void createWindow(final String id) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JDialog jDialog = new JDialog(new JFrame(), id, false);
				windows.put(id, jDialog);
				values.put(id, new HashMap<String, Object>());
				labels.put(id, new ArrayList<JLabel>());
				buttons.put(id, new ArrayList<JButton>());
				jDialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						disposeWindow(id);
					}
				});
				jDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				jDialog.setLayout(new FlowLayout());
				jDialog.setSize(500, 800);
				jDialog.setVisible(true);
			}
		});
	}

	@Override
	public void disposeWindow(String id) {
		if(windows.containsKey(id)) {
			windows.get(id).dispose();
			windows.get(id).getOwner().dispose();
			labels.remove(id);
			windows.remove(id);
			values.remove(id);
			Gdx.app.log("Window", "Closed " + id);
		}
	}

	@Override
	public void updateValues(String id, Map<String, Object> values) {
		if(this.values.containsKey(id)) {
			this.values.get(id).clear();
			this.values.get(id).putAll(values);
			updateLabels(id);
		}
	}

	@Override
	public void updateValue(String id, String name, Object value) {
		if(values.containsKey(id)) {
			values.get(id).put(name, value);
			updateLabels(id);
		}
	}

	@Override
	public void removeValue(String id, String name) {
		if(values.containsKey(id))
			values.get(id).remove(name);
	}

	@Override
	public void downloadAssets() {

	}

	@Override
	public void showBanner(boolean show) {

	}

	@Override
	public void showVideoAd() {

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

	@Override
	public boolean isProduction() {
		return IS_PRODUCTION;
	}

	public static void main (String[] args) {
		String mode = null;
		boolean isDebug = false;
		List<String> argList = Arrays.asList(args);
		if(argList.contains("debug")) {
			isDebug = true;
			argList.remove("debug");
		}
		if(argList.size() > 0)
			mode = argList.get(0);
		new DesktopLauncher(mode, isDebug);
		//DesktopControllerManager
	}
}