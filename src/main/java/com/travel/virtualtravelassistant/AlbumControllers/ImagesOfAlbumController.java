package com.travel.virtualtravelassistant.AlbumControllers;

import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImagesOfAlbumController {

    Album album;

    @FXML
    Label albumTitle;

    @FXML
    GridPane imagesGrid;

    @FXML
    Button saveChanges;

    private int currGridColumn = 0;
    private int currGridRow = 0;

    List<UserImage> newImages;

    public void setAlbum(Album album){
        this.album = album;
        albumTitle.setText(album.getTitle());
        FirestoreAction.getImages(album);
        loadImages();
    }

    public void initialize(){
        newImages = new ArrayList<>();
    }

    /***
     * Method for when add album button is clicked.
     * Gets album created locally and adds it to the newAlbums list for remote processing.
     */
    public void handleAddAlbumButton(){
        UserImage newImage = createImageForm();
        newImages.add(newImage);

        saveChanges.setVisible(true);
    }

    /***
     * Method for when save change button is clicked.
     * Saves all the albums in newAlbums to Firebase Storage and Firestore.
     */
    public void handleSaveChangesButton(){
        saveChanges.setVisible(false);

        for(UserImage image : newImages) {
            String httpURL = FirebaseStorageAction.uploadImageToAlbum(album, image);
            image.setImageURL(httpURL);

            FirestoreAction.storeImage(album, image);
        }
        newImages = new ArrayList<>();
    }


    /***
     * Loads create album form with title and file chooser.
     * @return album with info given in the form
     */
    private UserImage createImageForm(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/travel/virtualtravelassistant/createAlbumForm.fxml"));
        CreateAlbumFormController createAlbumFormController;
        try {
            BorderPane borderPane = loader.load();
            createAlbumFormController = loader.getController();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Album Information Form");
            popupStage.setScene(new Scene(borderPane));

            popupStage.showAndWait();

            String pathToImage = createAlbumFormController.getImage();
            String caption = createAlbumFormController.getTitle();

            UserImage userImage = new UserImage();
            userImage.setLocalPathToImage(pathToImage);
            userImage.setCaption(caption);

            loadAlbumPreview(userImage);

            return  userImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAlbumPreview(UserImage image){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/albumCard.fxml"));

        try {
            BorderPane borderPane = fxmlLoader.load();
            AlbumCardController albumCardController = fxmlLoader.getController();
            albumCardController.setAlbumInfoWithUserImage(image);
            albumCardController.viewAlbum.setVisible(false);
            if(currGridColumn == 3){
                currGridColumn = 0;
                currGridRow++;
            }
            imagesGrid.add(borderPane, currGridColumn++, currGridRow);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadImages(){

        List<UserImage> images = album.getImages();

        for(UserImage image : images) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/albumCard.fxml"));

            try {
                BorderPane borderPane = fxmlLoader.load();
                AlbumCardController albumCardController = fxmlLoader.getController();
                albumCardController.setAlbumInfoWithUserImageFromDB(image);
                albumCardController.viewAlbum.setVisible(false);
                if(currGridColumn == 3){
                    currGridColumn = 0;
                    currGridRow++;
                }
                imagesGrid.add(borderPane, currGridColumn++, currGridRow);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
