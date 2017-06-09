package com.logicmaster63.tdgalaxy.interfaces;

public interface GooglePlayServices {

    public void signIn();

    public void signOut();

    public void rateGame();

    public boolean isSignedIn();

    public void unlockAchievement(String name);

    public void showAchievements();
}
