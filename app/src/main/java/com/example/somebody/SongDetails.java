package com.example.somebody;

import java.io.Serializable;

public class SongDetails implements Serializable {
    String SongName, SongURL;

    public SongDetails() {
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getSongURL() {
        return SongURL;
    }

    public void setSongURL(String songURL) {
        SongURL = songURL;
    }
}
