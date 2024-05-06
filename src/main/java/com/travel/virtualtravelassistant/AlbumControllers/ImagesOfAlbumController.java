package com.travel.virtualtravelassistant.AlbumControllers;

import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    Map<UserImage, AlbumCardController> newImages;

    public void setAlbum(Album album){
        this.album = album;
        albumTitle.setText(album.getTitle());
        FirestoreAction.getImages(album);
        loadImages();
    }

    public void initialize(){
        newImages = new HashMap<>();
    }

    /***
     * Method for when add album button is clicked.
     * Gets album created locally and adds it to the newAlbums list for remote processing.
     */
    public void handleAddAlbumButton(){
        UserImage newImage = createImageForm();
        if(newImage != null) {
            AlbumCardController imageController = loadAlbumPreview(newImage);
            newImages.put(newImage, imageController);
            saveChanges.setVisible(true);
        }

    }

    /***
     * Method for when save change button is clicked.
     * Saves all the albums in newAlbums to Firebase Storage and Firestore.
     */
    public void handleSaveChangesButton(){
        saveChanges.setVisible(false);

        for(Map.Entry<UserImage, AlbumCardController> image : newImages.entrySet()) {
            UserImage userImage = image.getKey();
            String httpURL = FirebaseStorageAction.uploadImageToAlbum(album, userImage);
            userImage.setImageURL(httpURL);

            FirestoreAction.storeImage(album, userImage);
            image.getValue().setSavedFirebase(true);
        }
        newImages = new HashMap<>();
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
            popupStage.setTitle("Add image to " + album.getTitle() + " album");
            popupStage.setScene(new Scene(borderPane));
            createAlbumFormController.setImageForm(true);
            createAlbumFormController.setStage(popupStage);

//            popupStage.setOnCloseRequest(Event::consume);
            popupStage.showAndWait();

            boolean canceled = createAlbumFormController.getCancelled();
            if(canceled){
                return null;
            }

            String pathToImage = createAlbumFormController.getImage();
            String caption = createAlbumFormController.getCaption();

            if(pathToImage == null){
                return null;
            }

            UserImage userImage = new UserImage();
            userImage.setLocalPathToImage(pathToImage);
            userImage.setCaption(caption);

            return  userImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteImageFromNew(UserImage image){
        newImages.remove(image);
        currGridColumn--;
        if(currGridColumn == -1){
            currGridColumn = 2;
            currGridRow--;
        }

    }

    private AlbumCardController loadAlbumPreview(UserImage image){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/albumCard.fxml"));

        try {
            BorderPane borderPane = fxmlLoader.load();
            AlbumCardController albumCardController = fxmlLoader.getController();
            albumCardController.setAlbumInfoWithUserImage(image);
            albumCardController.setNode(borderPane);
            albumCardController.setCurrAlbum(album);
            albumCardController.setCurrUserImage(image);
            albumCardController.setParent(this);
            albumCardController.viewAlbum.setVisible(false);
            if(currGridColumn == 2){
                currGridColumn = 0;
                currGridRow++;
            }
            imagesGrid.add(borderPane, currGridColumn++, currGridRow);
            return albumCardController;
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
                albumCardController.setNode(borderPane);
                albumCardController.setCurrAlbum(album);
                albumCardController.setCurrUserImage(image);
                albumCardController.setSavedFirebase(true);
                albumCardController.setParent(this);
                albumCardController.viewAlbum.setVisible(false);
                if(currGridColumn == 2){
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
