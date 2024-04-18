package com.travel.virtualtravelassistant.AlbumControllers;

public class UserImage {
    private String imageURL;

    private String firestoreId;

    private String localPathToImage;

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
}
