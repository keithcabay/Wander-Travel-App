package com.travel.virtualtravelassistant.AlbumControllers;

import javafx.scene.image.Image;

public class UserImage {
    private Image currImage;

    private String imageURL;

    private String firestoreId;

    private String localPathToImage;

    private String caption;

    public Image getCurrImage() {
        return currImage;
    }

    public void setCurrImage(Image currImage) {
        this.currImage = currImage;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getFirestoreId() {
        return firestoreId;
    }

    public void setFirestoreId(String firestoreId) {
        this.firestoreId = firestoreId;
    }

    public String getLocalPathToImage() {
        return localPathToImage;
    }

    public void setLocalPathToImage(String localPathToImage) {
        this.localPathToImage = localPathToImage;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
