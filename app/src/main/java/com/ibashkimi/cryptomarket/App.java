package com.ibashkimi.cryptomarket;

import android.app.Application;


public class App extends Application {
    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static synchronized App getInstance() {
        return sInstance;
    }
}
