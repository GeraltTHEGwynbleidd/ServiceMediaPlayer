package com.gwynbleidd.servicemediaplayer.database;

import android.content.Context;
import android.util.Log;

import com.gwynbleidd.servicemediaplayer.BuildConfig;
import com.gwynbleidd.servicemediaplayer.database.entity.MusicObjs;
import com.gwynbleidd.servicemediaplayer.database.entity.MyObjectBox;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class ObjectBox {

    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();

        if (BuildConfig.DEBUG) {
            boolean started = new AndroidObjectBrowser(boxStore).start(context);
            Log.i("ObjectBrowser", "Started: " + started);
        }
    }

    public static BoxStore get() { return boxStore; }

    public static void DeleteMusic(long id){
        boxStore.boxFor(MusicObjs.class).remove(id);

    }

}
