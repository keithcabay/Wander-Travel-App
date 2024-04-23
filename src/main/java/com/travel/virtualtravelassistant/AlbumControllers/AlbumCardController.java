package com.travel.virtualtravelassistant.AlbumControllers;

import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    @FXML
    Button viewAlbum;

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
        Image image = new Image(album.getAlbumCover().getImageURL());
        album.setCurrentAlbumCoverImage(image);
        albumPreviewImage.setImage(image);
        albumPreviewImage.setFitHeight(450);
        title.setText(album.getTitle());
    }

    public void setAlbumInfoWithUserImageFromDB(UserImage image){
        if(image.getCaption() != null){
            title.setText(image.getCaption());
        }
        Image newImage = new Image(image.getImageURL());
        image.setCurrImage(newImage);
        albumPreviewImage.setImage(newImage);
        albumPreviewImage.setFitHeight(450);
    }

    public void setAlbumInfoWithUserImage(UserImage image){
        if(image.getCaption() != null){
            title.setText(image.getCaption());
        }
        Image preview = new Image("file:///" + image.getLocalPathToImage());
        System.out.println("file:///" + image.getLocalPathToImage());
        albumPreviewImage.setImage(preview);
        albumPreviewImage.setFitHeight(450);
    }

    public void handleViewAlbumButton(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/travel/virtualtravelassistant/imagesOfAlbum.fxml"));
            BorderPane borderPane = loader.load();

            // Pass data to the controller of the next scene
            ImagesOfAlbumController imagesOfAlbumController = loader.getController();
            imagesOfAlbumController.setAlbum(currAlbum);

            Scene scene = new Scene(borderPane);

            // Get the Stage from the ActionEvent
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
