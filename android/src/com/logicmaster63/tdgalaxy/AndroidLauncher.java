package com.logicmaster63.tdgalaxy;

import android.app.backup.BackupAgentHelper;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.google.android.gms.games.Games;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.testing.json.MockJsonFactory;
import com.google.api.services.gamesManagement.GamesManagement;
import com.logicmaster63.tdgalaxy.constants.Constants;
import com.logicmaster63.tdgalaxy.interfaces.FileStuff;
import com.logicmaster63.tdgalaxy.interfaces.OnlineServices;
import com.logicmaster63.tdgalaxy.screens.GameScreen;
import com.logicmaster63.tdgalaxy.tools.ArchiveFileHandleResolver;
import com.logicmaster63.tdgalaxy.tools.Tools;
import dalvik.system.DexFile;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.zip.ZipFile;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener, OnlineServices, FileStuff {

	private static final int REQUEST_ACHIEVEMENTS = 5001;

	private GameHelper gameHelper;
	private BackupAgentHelper helper;

	@Override
	public void signOut() {
		gameHelper.signOut();
	}

	@Override
	public void rateGame() {
		final String appPackageName = getPackageName();
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
		}
	}

	@Override
	public boolean isSignedIn() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void resetAchievement(String name) {
		//ArrayList<String> scopes = new ArrayList<>();
		//scopes.add(Scopes.GAMES);
		//GoogleAuthorizationCodeFlow codeFlow = new GoogleAuthorizationCodeFlow(new NetHttpTransport(), new MockJsonFactory(), "", "", scopes);
		//codeFlow.loadCredential()
		//GoogleCredential credential = new GoogleCredential().setAccessToken();

		GamesManagement client = new GamesManagement.Builder(new NetHttpTransport(), new MockJsonFactory(),null).setApplicationName("TDGalaxy").build();

		Gdx.app.error("Google Play Services", client.toString());

		try {
			Gdx.app.error("Google Play Services", client.achievements().resetAll().execute().toString());
		} catch (IOException e) {
			Gdx.app.error("Google Play Services", e.toString());
		}
	}

	@Override
	public void onSignInFailed() {
		Gdx.app.error("Google Play Services", "Sign in Failed");
	}

	@Override
	public void onSignInSucceeded() {
		Gdx.app.error("Google Play Services", "Sign in Succeeded");
	}

	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		gameHelper.onStart(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void signIn() {
		gameHelper.beginUserInitiatedSignIn();
	}

	@Override
	public void unlockAchievement(String name) {
		Games.Achievements.unlock(gameHelper.getApiClient(), name);
	}

	@Override
	public void showAchievements() {
		if(isSignedIn())
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), REQUEST_ACHIEVEMENTS);
		else
			signIn();
	}

	@Override
	public void submitScore(String name, long highScore) {
		if(isSignedIn())
			Games.Leaderboards.submitScore(gameHelper.getApiClient(), name, highScore);
		else
			signIn();
	}

	@Override
	public void showScore(String name) {
		if(isSignedIn())
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), name), REQUEST_ACHIEVEMENTS);
		else
			signIn();
	}

	@Override
	public void saveGame(String name) {

	}

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

	@Override
	public AssetManager getExternalAssets() {
		AssetManager externalAssets = null;

		try {
			String[] files = getAPKExpansionFiles(this, getPackageManager().getPackageInfo(getPackageName(), 0).versionCode, Constants.EXPANSION_FILE_VERSION);
			ZipFile archive = new ZipFile(new File(files[0]));
			ArchiveFileHandleResolver resolver = new ArchiveFileHandleResolver(archive);
			externalAssets = new AssetManager(resolver);
			//externalAssets.setLoader(Music.class, new MusicLoader(resolver));
		} catch (IOException | PackageManager.NameNotFoundException e) {
			System.err.println(e.toString());
		}
		return externalAssets;
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES | GameHelper.CLIENT_SNAPSHOT);
		gameHelper.setup(this);
		helper = new BackupAgentHelper();
		//gameHelper.setConnectOnStart(true);

		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;
		config.useWakelock = true;
		config.useImmersiveMode = false;
		config.useGyroscope = true;
		initialize(new TDGalaxy(this, null, this), config);

	}

	private final static String EXP_PATH = "/Android/obb/";

	static String[] getAPKExpansionFiles(Context ctx, int mainVersion, int patchVersion) {
		String packageName = ctx.getPackageName();
		Vector<String> ret = new Vector<String>();
		if (Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED)) {
			// Build the full path to the app's expansion files
			File root = Environment.getExternalStorageDirectory();
			File expPath = new File(root.toString() + EXP_PATH + packageName);

			// Check that expansion file path exists
			if (expPath.exists()) {
				if ( mainVersion > 0 ) {
					String strMainPath = expPath + File.separator + "main." +
							mainVersion + "." + packageName + ".obb";
					File main = new File(strMainPath);
					if ( main.isFile() ) {
						ret.add(strMainPath);
					}
				}
				if ( patchVersion > 0 ) {
					String strPatchPath = expPath + File.separator + "patch." +
							mainVersion + "." + packageName + ".obb";
					File main = new File(strPatchPath);
					if ( main.isFile() ) {
						ret.add(strPatchPath);
					}
				}
			}
		}
		String[] retArray = new String[ret.size()];
		ret.toArray(retArray);
		return retArray;
	}
}
