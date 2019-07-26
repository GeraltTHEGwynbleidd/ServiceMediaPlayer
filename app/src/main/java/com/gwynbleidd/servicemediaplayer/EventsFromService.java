package com.gwynbleidd.servicemediaplayer;

import com.gwynbleidd.servicemediaplayer.database.entity.MusicObjs;

import java.util.List;

public class EventsFromService {

    public static class StateEvent{

        public final String currentState;

        public StateEvent(String currentState) {
            this.currentState = currentState;
        }
    }

    public static class SongInfoSend{
        public final MusicObjs musicObjs;

        public SongInfoSend(MusicObjs musicObjs) {
            this.musicObjs = musicObjs;
        }
    }

    public static class PlaybackPosition {

        public final int position;

        public PlaybackPosition(int position) {
            this.position = position;
        }
    }

    public static class PlaybackDuration {

        public final int duration;

        public PlaybackDuration(int duration) {
            this.duration = duration;
        }
    }

    public static class DatabaseIsUpdate {
        public final List<MusicObjs> musicObjsList;

        public DatabaseIsUpdate(List<MusicObjs> musicObjsList) {
            this.musicObjsList = musicObjsList;
        }
    }

}
