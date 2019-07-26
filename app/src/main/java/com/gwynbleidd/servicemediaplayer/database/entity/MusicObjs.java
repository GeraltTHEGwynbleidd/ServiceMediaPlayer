package com.gwynbleidd.servicemediaplayer.database.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class MusicObjs {

    @Id
    public long musicId;

    public String musicName;
    public String musicUrl;
    public String musicArtist;
    public String musicAlbum;
    public int musicDuration;
    public String musicDatemodified;



    public MusicObjs() {
    }

    public MusicObjs(long musicId, String musicName) {
        this.musicId = musicId;
        this.musicName = musicName;
    }

    public long getMusicId() {
        return musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getMusicArtist() {
        return musicArtist;
    }

    public void setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
    }

    public String getMusicAlbum() {
        return musicAlbum;
    }

    public void setMusicAlbum(String musicAlbum) {
        this.musicAlbum = musicAlbum;
    }

    public int getMusicDuration() {
        return musicDuration;
    }

    public void setMusicDuration(int musicDuration) {
        this.musicDuration = musicDuration;
    }

    public String getMusicDatemodified() {
        return musicDatemodified;
    }

    public void setMusicDatemodified(String musicDatemodified) {
        this.musicDatemodified = musicDatemodified;
    }
}
