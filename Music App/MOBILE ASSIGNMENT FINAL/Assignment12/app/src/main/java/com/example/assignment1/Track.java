package com.example.assignment1;
import java.io.Serializable;
public class Track implements Serializable{
    private String trackName;
    private String genreName;
    private int id;
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getGenreName(){
        return genreName;
    }

    public void setGenreName(String genreName){
        this.genreName = genreName;
    }

    public String getTrackName(){
        return trackName;
    }

    public void setTrackName (String trackName){
        this.trackName = trackName;
    }

    public String toString(){
        String s = "Track: " + trackName + "\n";
        s += "Genre: " + genreName + "\n";
        return s;
    }


}
