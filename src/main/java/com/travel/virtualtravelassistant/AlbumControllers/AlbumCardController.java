package com.travel.virtualtravelassistant.AlbumControllers;

import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.io.IOException;

public class AlbumCardController {

    Album currAlbum;

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

    public Album getCurrAlbum() {
        return currAlbum;
    }

    public void setCurrAlbum(Album currAlbum) {
        this.currAlbum = currAlbum;
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
