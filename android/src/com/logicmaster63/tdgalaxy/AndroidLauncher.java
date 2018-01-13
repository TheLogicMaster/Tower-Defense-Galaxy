package com.logicmaster63.tdgalaxy;

import android.app.backup.BackupAgentHelper;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.games.Games;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.testing.json.MockJsonFactory;
import com.google.api.services.gamesManagement.GamesManagement;
import com.logicmaster63.tdgalaxy.constants.Constants;
import com.logicmaster63.tdgalaxy.constants.GameMode;
import com.logicmaster63.tdgalaxy.interfaces.FileStuff;
import com.logicmaster63.tdgalaxy.interfaces.OnlineServices;
import com.logicmaster63.tdgalaxy.tools.ArchiveFileHandleResolver;
import dalvik.system.DexFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipFile;

public class AndroidLauncher extends AndroidApplication implements GameHelper.GameHelperListener, OnlineServices, FileStuff, RewardedVideoAdListener {

    private static final int REQUEST_ACHIEVEMENTS = 5001;

    private GameHelper gameHelper;
    private BackupAgentHelper helper;
    private AdView banner;
    private RewardedVideoAd rewardVideo;
    private boolean loadingAd = false;

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

        GamesManagement client = new GamesManagement.Builder(new NetHttpTransport(), new MockJsonFactory(), null).setApplicationName("TDGalaxy").build();

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
        if (isSignedIn())
            startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), REQUEST_ACHIEVEMENTS);
        else
            signIn();
    }

    @Override
    public void submitScore(String name, long highScore) {
        if (isSignedIn())
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), name, highScore);
        else
            signIn();
    }

    @Override
    public void showScore(String name) {
        if (isSignedIn())
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), name), REQUEST_ACHIEVEMENTS);
        else
            signIn();
    }

    @Override
    public void saveGame(String name) {

    }

    @Override
    public void downloadAssets() {

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
            String[] files = getAPKExpansionFiles(this, Constants.EXPANSION_FILE_VERSION, 0);
            if (files.length > 0) {
                ZipFile archive = new ZipFile(new File(files[0]));
                externalAssets = new AssetManager(new ArchiveFileHandleResolver(archive));
            } else {
                Gdx.app.error("AndroidLauncher", "Couldn't load .obb file");
            }
        } catch (IOException e) {
            Gdx.app.error("Get external assets", e.toString());
        }
        return externalAssets;
    }

    @Override
    public void showBanner(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                banner.loadAd(new AdRequest.Builder().build());
                banner.setVisibility(show ? 0 : 4);
            }
        });
    }

    @Override
    public void showVideoAd() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rewardVideo.isLoaded())
                    rewardVideo.show();
                else {
                    rewardVideo.loadAd("ca-app-pub-9741625146217992/5189319253", new AdRequest.Builder().build());
                    loadingAd = true;
                }
            }
        });
    }

    @Override
    public boolean isProduction() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String mode = null;
        if(getIntent().getExtras() != null)
            mode = getIntent().getExtras().getString("mode");

        if (Build.VERSION.SDK_INT < 16)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES | GameHelper.CLIENT_SNAPSHOT);
        gameHelper.setup(this);
        helper = new BackupAgentHelper();
        //gameHelper.setConnectOnStart(true);

        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = true;
        config.useWakelock = true;
        config.useImmersiveMode = false;
        config.useGyroscope = true;
        //initialize(new TDGalaxy(this, null, this), config);
        View view = initializeForView(new TDGalaxy(mode,this, null, this, null), config);
        RelativeLayout layout = new RelativeLayout(this);
        MobileAds.initialize(this, Constants.ADSENSE_ID);

        banner = new AdView(this);
        banner.setAdSize(AdSize.SMART_BANNER);
        banner.setAdUnitId(Constants.AD_BANNER);
        banner.setBackgroundColor(Color.BLACK);

        rewardVideo = MobileAds.getRewardedVideoAdInstance(this);
        rewardVideo.setRewardedVideoAdListener(this);
        rewardVideo.loadAd(Constants.AD_VIDEO, new AdRequest.Builder().build());

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(view);
        layout.addView(banner, adParams);
        setContentView(layout);
        banner.setVisibility(View.VISIBLE);
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
                if (mainVersion > 0) {
                    String strMainPath = expPath + File.separator + "main." +
                            mainVersion + "." + packageName + ".obb";
                    File main = new File(strMainPath);
                    if (main.isFile()) {
                        ret.add(strMainPath);
                    }
                }
                if (patchVersion > 0) {
                    String strPatchPath = expPath + File.separator + "patch." +
                            mainVersion + "." + packageName + ".obb";
                    File main = new File(strPatchPath);
                    if (main.isFile()) {
                        ret.add(strPatchPath);
                    }
                }
            }
        }
        String[] retArray = new String[ret.size()];
        ret.toArray(retArray);
        return retArray;
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        if (loadingAd) {
            rewardVideo.show();
            loadingAd = false;
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        rewardVideo.loadAd("ca-app-pub-9741625146217992/5189319253", new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        TDGalaxy.preferences.changePref("money", TDGalaxy.preferences.getMoney() + 100);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Gdx.app.error("RewardedAd", Integer.toString(i));
    }
}
