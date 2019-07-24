package com.gwynbleidd.servicemediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    Button buttonPlay, buttonPause;
    SeekBar seekbarPlay;
    boolean isUserSeeking;
    MediaPlayerReciver mediaPlayerReciver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayerReciver = new MediaPlayerReciver();
        registerReceiver(mediaPlayerReciver,new IntentFilter(Intent.ACTION_HEADSET_PLUG));

        EventBus.getDefault().register(this);
        startService(new Intent(this, MediaPlayerService.class));

        buttonPlay = findViewById(R.id.button_play);
        buttonPause = findViewById(R.id.button_pause);
        seekbarPlay = findViewById(R.id.seekbar_player);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventsFromMainActivity.PlayEvent());
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventsFromMainActivity.PauseEvent());
            }
        });

        seekbarPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int userSelectedPosition = 0;


            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    userSelectedPosition = i;
                    isUserSeeking = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserSeeking = false;
                EventBus.getDefault().post(new EventsFromMainActivity.SeekToEvent(userSelectedPosition));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mediaPlayerReciver);
    }

    @Subscribe
    public void onMessageEvent(EventsFromService.StateEvent stateEvent) {
//        Toast.makeText(this, stateEvent.currentState + "", Toast.LENGTH_SHORT).show();
    }

    @Subscribe()
    public void onMessageEvent(EventsFromService.PlaybackDuration event) {
        seekbarPlay.setMax(event.duration);
    }

    @Subscribe()
    public void onMessageEvent(EventsFromService.PlaybackPosition playbackPosition) {
        if (!isUserSeeking) {
            seekbarPlay.setProgress(playbackPosition.position);
        }
    }

//    private class MusicIntentReceiver extends BroadcastReceiver {
//        String TAG = "Receiverlog";
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
//                int state = intent.getIntExtra("state", -1);
//                switch (state) {
//                    case 0:
//                        Log.d(TAG, "Headset is unplugged");
//                        break;
//                    case 1:
//                        Log.d(TAG, "Headset is plugged");
//                        break;
//                    default:
//                        Log.d(TAG, "I have no idea what the headset state is");
//                }
//            }
//        }
//    }

}
