package com.travel.virtualtravelassistant;

import com.google.cloud.ByteArray;
import javafx.scene.image.Image;

import java.util.List;

public class Album {
    private String albumCover;

    private String albumCoverURL;

    private Image currImage;

    private String title;

    private List<String> unloaded;

    private List<ByteArray> loaded;

    public Album(){}

    public Album(String albumCover, String title){
        this.albumCover = albumCover;
        this.title = title;
    }

    public Album(Image currImage, String title){
        this.currImage = currImage;
        this.title = title;
    }

    public String getAlbumCoverURL(){return albumCoverURL;}

    public void setAlbumCoverURL(String albumCoverURL){this.albumCoverURL = albumCoverURL;};

    public Image getCurrImage(){return currImage;}

    public void setCurrImage(Image image){this.currImage = currImage;}

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
