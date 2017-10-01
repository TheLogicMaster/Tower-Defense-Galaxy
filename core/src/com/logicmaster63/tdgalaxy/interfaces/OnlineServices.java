package com.logicmaster63.tdgalaxy.interfaces;

public interface OnlineServices {

    void signIn();

    void signOut();

    void rateGame();

    boolean isSignedIn();

    void unlockAchievement(String name);

    void showAchievements();

    void resetAchievement(String name);

    void showScore(String name);

    void submitScore(String name, long highScore);

    void saveGame(String name);

    void showBanner(boolean show);

    //void updateBanner();

    void showVideoAd();
}
