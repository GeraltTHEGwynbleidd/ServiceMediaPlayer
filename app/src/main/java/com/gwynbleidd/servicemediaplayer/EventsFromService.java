package com.gwynbleidd.servicemediaplayer;

public class EventsFromService {

    public static class StateEvent{

        public final String currentState;

        public StateEvent(String currentState) {
            this.currentState = currentState;
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

}
