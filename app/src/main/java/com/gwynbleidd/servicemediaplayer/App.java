package com.gwynbleidd.servicemediaplayer;

import android.app.Application;

import com.gwynbleidd.servicemediaplayer.database.ObjectBox;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.init(this);
    }
}
