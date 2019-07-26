package com.gwynbleidd.servicemediaplayer;

import com.gwynbleidd.servicemediaplayer.database.entity.MusicObjs;

public class EventsFromMainActivity {

    public static class PlayEvent {

    }

    public static class PauseEvent {

    }

    public static class StopEvent {

    }

    public static class LoadEvent {

    }

    public static class SeekToEvent {

        public final int position;

        public SeekToEvent(int position) {
            this.position = position;
        }
    }

    public static class LoadSelectedFile{
        public final MusicObjs musicObjs;

        public LoadSelectedFile(MusicObjs musicObjs) {
            this.musicObjs = musicObjs;
        }
    }

    public static class NumOfSongsFromAdapter {

        public final int numOfSongs;

        public NumOfSongsFromAdapter(int numOfSongs) {
            this.numOfSongs = numOfSongs;
        }
    }


}
