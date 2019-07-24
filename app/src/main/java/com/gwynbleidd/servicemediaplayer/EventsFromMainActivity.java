package com.gwynbleidd.servicemediaplayer;

public class EventsFromMainActivity {

    public static class PlayEvent {

    }

    public static class PauseEvent {

    }

    public static class SeekToEvent {

        public final int position;

        public SeekToEvent(int position) {
            this.position = position;
        }
    }

}
