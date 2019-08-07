package com.gwynbleidd.servicemediaplayer;

import com.gwynbleidd.servicemediaplayer.database.entity.MusicObjs;

import java.util.List;

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
        public final int position;

        public LoadSelectedFile(MusicObjs musicObjs, int position) {
            this.musicObjs = musicObjs;
            this.position = position;
        }
    }

    public static class UpdatePlayingposition{
        public final int pos;

        public UpdatePlayingposition(int pos) {
            this.pos = pos;
        }
    }

    public static class NumOfSongsFromAdapter {

        public final int numOfSongs;

        public NumOfSongsFromAdapter(int numOfSongs) {
            this.numOfSongs = numOfSongs;
        }
    }

    public static class DataSetFromAdapter {

        public final List<MusicObjs> musicObjsList;

        public DataSetFromAdapter(List<MusicObjs> musicObjsList) {
            this.musicObjsList = musicObjsList;
        }
    }


}
