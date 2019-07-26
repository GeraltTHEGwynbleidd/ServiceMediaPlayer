package com.gwynbleidd.servicemediaplayer;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import com.gwynbleidd.servicemediaplayer.database.ObjectBox;
import com.gwynbleidd.servicemediaplayer.database.entity.MusicObjs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.objectbox.Box;

public class MediaPlayerService extends Service {

    MediaPlayer mediaPlayer=new MediaPlayer();

    Box<MusicObjs> musicObjsBox;

    ScheduledExecutorService mExecutor;
    Runnable mSeekbarProgressUpdateTask;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

//        mediaPlayer = MediaPlayer.create(this, R.raw.music_01);
//        EventBus.getDefault().post(new EventsFromService.PlaybackDuration(mediaPlayer.getDuration()));
        musicObjsBox = ObjectBox.get().boxFor(MusicObjs.class);
        boolean FirstTime = getSharedPreferences("APP_INFO", MODE_PRIVATE).getBoolean("FirstTime", true);
        if (FirstTime) {
            loadFromStorage();
            getSharedPreferences("APP_INFO", MODE_PRIVATE).edit().putBoolean("FirstTime", false).apply();
        }
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

    public void stop() {
            mediaPlayer.pause();
            seekTo(0);
            EventBus.getDefault().post(new EventsFromService.StateEvent("stop"));
            EventBus.getDefault().post(new EventsFromService.PlaybackPosition(0));

    }

    public void load(MusicObjs musicObjs) {
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(this,Uri.parse(musicObjs.getMusicUrl()));
        EventBus.getDefault().post(new EventsFromService.PlaybackDuration(mediaPlayer.getDuration()));
        EventBus.getDefault().post(new EventsFromService.SongInfoSend(musicObjs));
        play();
    }

    private void loadFromStorage() {

        musicObjsBox.removeAll();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String dateModified = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED));

                    MusicObjs musicObjs = new MusicObjs();
                    musicObjs.setMusicName(name);
                    musicObjs.setMusicArtist(artist);
                    musicObjs.setMusicUrl(url);
                    musicObjs.setMusicAlbum(album);
                    musicObjs.setMusicDuration(Integer.parseInt(duration)/1000);
                    musicObjs.setMusicDatemodified(dateModified);

                    musicObjsBox.put(musicObjs);


//                    SongInfo s = new SongInfo(name, artist, url);
//                    _songs.add(s);

                } while (cursor.moveToNext());

            }

            cursor.close();
            EventBus.getDefault().post(new EventsFromService.DatabaseIsUpdate(musicObjsBox.getAll()));
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
    public void onMessagePlay(EventsFromMainActivity.PlayEvent playEvent) {
        play();
    }

    @Subscribe()
    public void onMessagePause(EventsFromMainActivity.PauseEvent pauseEvent) {
        pause();
    }

    @Subscribe()
    public void onMessageStop(EventsFromMainActivity.StopEvent stopEvent) {
        stop();
    }

    @Subscribe()
    public void onMessageLoadMusics(EventsFromMainActivity.LoadEvent loadEvent) {
        stop();
        loadFromStorage();
    }

    @Subscribe()
    public void onMessageSeekMediaPlayerto(EventsFromMainActivity.SeekToEvent event) {
        seekTo(event.position);
    }

    @Subscribe()
    public void onMessagePlaySelectedMusic(EventsFromMainActivity.LoadSelectedFile loadSelectedFile) {
        load(loadSelectedFile.musicObjs);
    }


}
