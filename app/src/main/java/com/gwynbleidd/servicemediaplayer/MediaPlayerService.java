package com.gwynbleidd.servicemediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MediaPlayerService extends Service {

    MediaPlayer mediaPlayer;

    ScheduledExecutorService mExecutor;
    Runnable mSeekbarProgressUpdateTask;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.music_01);
        EventBus.getDefault().post(new EventsFromService.PlaybackDuration(mediaPlayer.getDuration()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EventBus.getDefault().post(new EventsFromService.PlaybackDuration(mediaPlayer.getDuration()));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    public void play() {
        if (!mediaPlayer.isPlaying()) {
            EventBus.getDefault().post(new EventsFromService.StateEvent("play"));
            mediaPlayer.start();
            startUpdatingSeekbarWithPlaybackProgress();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            EventBus.getDefault().post(new EventsFromService.StateEvent("pause"));
            mediaPlayer.pause();
        }
    }

    public void seekTo(int duration) {
        mediaPlayer.seekTo(duration);
    }

    private void startUpdatingSeekbarWithPlaybackProgress() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        if (mSeekbarProgressUpdateTask == null) {
            mSeekbarProgressUpdateTask = new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        EventBus.getDefault().post(new EventsFromService.PlaybackPosition(currentPosition));
                    }
                }
            };
        }
        mExecutor.scheduleAtFixedRate(mSeekbarProgressUpdateTask, 0, 1000, TimeUnit.MILLISECONDS);
    }


    @Subscribe()
    public void onMessageEvent(EventsFromMainActivity.PlayEvent playEvent) {
        play();
    }

    @Subscribe()
    public void onMessageEvent(EventsFromMainActivity.PauseEvent pauseEvent) {
        pause();
    }

    @Subscribe()
    public void onMessageEvent(EventsFromMainActivity.SeekToEvent event) {
        seekTo(event.position);
    }


}
