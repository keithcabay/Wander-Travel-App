package com.travel.virtualtravelassistant.AlbumControllers;

import com.google.cloud.ByteArray;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Album {

    private String firestoreId;

    private String localPathToCover;

    private UserImage albumCover;

    private Image currentAlbumCoverImage;

    private String title;

    private List<UserImage> images;

    private List<ByteArray> loaded;

    public Album(){
        images = new ArrayList<>();
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public void setFirestoreId(String firestoreId) {
        this.firestoreId = firestoreId;
    }

    public UserImage getAlbumCover(){
        return albumCover;
    }

    public void setAlbumCover(UserImage albumCover){
        this.albumCover = albumCover;
    };

    public Image getCurrentAlbumCoverImage(){
        return currentAlbumCoverImage;
    }

    public void setCurrentAlbumCoverImage(Image image){
        this.currentAlbumCoverImage = image;
    }

    public String getLocalPathToCover() {
        return localPathToCover;
    }
    public void setLocalPathToCover(String localPathToCover) {
        this.localPathToCover = localPathToCover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addImage(UserImage userImage){
        images.add(userImage);
    }

    public List<UserImage> getImages(){
        return images;
    }
}
