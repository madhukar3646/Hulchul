package com.app.zippnews.model;

public class SongsModel {

    private String songtitle,songartist,displayname,songpath;
    private boolean isPlaying = false;
    public String getSongtitle() {
        return songtitle;
    }

    public void setSongtitle(String songtitle) {
        this.songtitle = songtitle;
    }

    public String getSongartist() {
        return songartist;
    }

    public void setSongartist(String songartist) {
        this.songartist = songartist;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getSongpath() {
        return songpath;
    }

    public void setSongpath(String songpath) {
        this.songpath = songpath;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
