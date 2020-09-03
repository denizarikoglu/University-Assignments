package com.example.assignment1;

import java.io.Serializable;

public class Artist implements Serializable{
    private String artistName;
    private String trackName;
    private int id;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getArtistname(){
        return artistName;
    }

    public void setArtistName(String artistName){
        this.artistName = artistName;
    }

    public String getTrackName(){
        return trackName;
    }

    public void setTrackName (String trackName){
        this.trackName = trackName;
    }

    public String toString(){
        String s = "Artist: " + artistName + "\n";
        s += "Track: " + trackName + "\n";
        return s;
    }
}
