package com.ibashkimi.cryptomarket;

import android.app.Application;

public class App extends Application {
    private static App sInstance;

    public static synchronized App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
