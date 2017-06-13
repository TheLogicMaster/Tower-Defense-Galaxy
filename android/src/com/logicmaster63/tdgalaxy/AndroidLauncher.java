package com.logicmaster63.tdgalaxy;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.testing.json.MockJsonFactory;
import com.google.api.services.gamesManagement.GamesManagement;
import com.google.api.services.gamesManagement.GamesManagementRequest;
import com.google.api.services.gamesManagement.GamesManagementRequestInitializer;
import com.google.api.services.gamesManagement.GamesManagementScopes;
import com.google.common.base.FinalizableReference;
import com.logicmaster63.tdgalaxy.interfaces.FileStuff;
import com.logicmaster63.tdgalaxy.interfaces.GooglePlayServices;
import dalvik.system.DexFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener, GooglePlayServices{

	private static final int REQUEST_ACHIEVEMENTS = 5001;

	private GameHelper gameHelper;

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
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.setup(this);
		//gameHelper.setConnectOnStart(true);

		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;
		config.useWakelock = true;
		config.useImmersiveMode = false;
		config.useGyroscope = true;
		initialize(new TDGalaxy(new FileStuff() {
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
		}, null, this), config);
	}
}
