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

public class AndroidLauncher extends AndroidApplication implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GooglePlayServices {

	private static final int REQUEST_ACHIEVEMENTS = 5001;
	private static final int REQUEST_RESOLVE_ERROR = 1001;
	private static final String STATE_RESOLVING_ERROR = "resolving_error";

	private GoogleApiClient mGoogleApiClient;
	private boolean mResolvingError = false;


	@Override
	public void signOut() {
		Games.signOut(mGoogleApiClient);
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
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
		return mGoogleApiClient != null && mGoogleApiClient.isConnected();
	}

	@Override
	public void resetAchievement(String name) {
		//ArrayList<String> scopes = new ArrayList<>();
		//scopes.add(Scopes.GAMES);
		//GoogleAuthorizationCodeFlow codeFlow = new GoogleAuthorizationCodeFlow(new NetHttpTransport(), new MockJsonFactory(), "", "", scopes);
		//codeFlow.loadCredential()
		//GoogleCredential credential = new GoogleCredential().setAccessToken();

		GamesManagement client = new GamesManagement.Builder(new NetHttpTransport(), new MockJsonFactory(),null)
				.setApplicationName("TDGalaxy").build();

		Gdx.app.error("Google Play Services", client.toString());

		try {
			Gdx.app.error("Google Play Services", client.achievements().resetAll().execute().toString());
		} catch (IOException e) {
			Gdx.app.error("Google Play Services", e.toString());
		}
	}

	@Override
	public void unlockAchievement(String name) {
		if(isSignedIn())
			Games.Achievements.unlock(mGoogleApiClient, name);
	}

	@Override
	public void showAchievements() {
		if(isSignedIn())
			startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult result) {
			if (mResolvingError) {
			return;
		} else if (result.hasResolution()) {
			try {
				mResolvingError = true;
				result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
			} catch (IntentSender.SendIntentException e) {
				mGoogleApiClient.connect();
			}
		} else {
			mResolvingError = true;
		}
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		Gdx.app.debug("Google Play Services", "Connected");
	}

	@Override
	public void onConnectionSuspended(int i) {
		Gdx.app.debug("Google Play Services", "Connection Suspended");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mResolvingError = savedInstanceState != null && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

		new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).requestEmail().build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Games.API).addScope(Games.SCOPE_GAMES)
				.build();

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

	@Override
	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_RESOLVE_ERROR) {
			mResolvingError = false;
			mGoogleApiClient.connect();
		}
	}

	@Override
	public void signIn() {
		mGoogleApiClient.connect();
	}
}
