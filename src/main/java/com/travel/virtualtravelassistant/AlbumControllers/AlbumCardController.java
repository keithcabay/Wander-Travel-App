package com.travel.virtualtravelassistant.AlbumControllers;

import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AlbumCardController {
    @FXML
    ImageView albumPreviewImage;

    @FXML
    Label title;

    public ImageView getAlbumPreviewImage() {
        return albumPreviewImage;
    }

    public void setAlbumPreviewImage(ImageView albumPreviewImage) {
        this.albumPreviewImage = albumPreviewImage;
    }

    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    public void setAlbumInfoWithImage(Album album){
        Image image = new Image("file:///" + album.getLocalPathToCover());
        System.out.println("file:///" + album.getLocalPathToCover());
        albumPreviewImage.setImage(image);
        albumPreviewImage.setFitHeight(450);
        title.setText(album.getTitle());
    }

    public void setAlbumInfoWithURL(Album album){
        Image image = FirebaseStorageAction.getImageWithURL(album.getAlbumCover());
        albumPreviewImage.setImage(image);
        albumPreviewImage.setFitHeight(450);
        title.setText(album.getTitle());
    }
}
