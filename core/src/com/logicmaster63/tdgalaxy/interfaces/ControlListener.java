package com.logicmaster63.tdgalaxy.interfaces;

public interface ControlListener {

    boolean onPause(boolean reconnect, int controllersNeeded);

    boolean onResume();
}
