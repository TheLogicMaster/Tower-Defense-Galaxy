package com.logicmaster63.tdgalaxy;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.lang.Process;

public class PackIsolationService extends Service {

    /** indicates how to behave if the service is killed */
    int mStartMode;

    /** interface for clients that bind */
    private final PackIsolation.Stub mBinder = new PackIsolation.Stub() {
        public int test(){
            return 69;
        }
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) {
            // Does nothing
        }
    };

    /** indicates whether onRebind should be used */
    boolean mAllowRebind = false;

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return mStartMode;
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {

    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}