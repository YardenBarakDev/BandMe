package com.bawp.bandme.util;

import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FireBaseMethods.initHelper();
    }
}
