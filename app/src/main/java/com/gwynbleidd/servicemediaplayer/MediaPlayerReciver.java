package com.gwynbleidd.servicemediaplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class MediaPlayerReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    Toast.makeText(context, "unPluged", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new EventsFromMainActivity.PauseEvent());
                    break;
                case 1:
                    Toast.makeText(context, "Pluged", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new EventsFromMainActivity.PlayEvent());
                    break;
            }
        }
    }
}
