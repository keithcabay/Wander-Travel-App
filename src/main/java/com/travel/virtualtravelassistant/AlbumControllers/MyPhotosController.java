package com.travel.virtualtravelassistant.AlbumControllers;

import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyPhotosController {
    @FXML
    GridPane albumsGrid;
    @FXML
    Button saveChanges;
    private int currGridColumn = 0;
    private final int currGridRow = 0;
    private List<Album> newAlbums = new ArrayList<>();


    /***
     * On page load albums information will be fetched from Firestore and loaded from Firebase storage
     */
    public void initialize(){
        List<Album> albums = FirestoreAction.getAlbums();

        for(Album album : albums){
            Image image = new Image(album.getAlbumCover().getImageURL());
            album.setCurrentAlbumCoverImage(image);
            loadAlbumPreviewWithDB(album);
        }
    }


    /***
     * Method for when save change button is clicked.
     * Saves all the albums in newAlbums to Firebase Storage and Firestore.
     */
    public void handleSaveChangesButton(){
        saveChanges.setVisible(false);

        for(Album album : newAlbums) {
            String httpURL = FirebaseStorageAction.uploadImageToAlbum(album, album.getAlbumCover());
            album.getAlbumCover().setImageURL(httpURL);

            FirestoreAction.storeAlbumInfo(album);
        }
        newAlbums = new ArrayList<>();
    }


    /***
     * Method for when add album button is clicked.
     * Gets album created locally and adds it to the newAlbums list for remote processing.
     */
    public void handleAddAlbumButton(){
        Album newAlbum = createAlbumForm();
        newAlbums.add(newAlbum);

        saveChanges.setVisible(true);
    }


    /***
     * Loads create album form with title and file chooser.
     * @return album with info given in the form
     */
    private Album createAlbumForm(){
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
            String title = createAlbumFormController.getTitle();

            UserImage userImage = new UserImage();
            userImage.setLocalPathToImage(pathToImage);

            Album newAlbum = new Album();
            newAlbum.setLocalPathToCover(userImage.getLocalPathToImage());
            newAlbum.setTitle(title);
            newAlbum.setAlbumCover(userImage);

            loadAlbumPreview(newAlbum);

            return  newAlbum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //load album preview card with given album information
    private void loadAlbumPreview(Album album){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/albumCard.fxml"));

        try {
            BorderPane borderPane = fxmlLoader.load();
            AlbumCardController albumCardController = fxmlLoader.getController();
            albumCardController.setAlbumInfoWithImage(album);
            albumsGrid.add(borderPane, currGridColumn++, currGridRow);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //load empty album preview card
    private void loadAlbumPreviewWithDB(Album album){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/albumCard.fxml"));

        try {
            BorderPane borderPane = fxmlLoader.load();
            AlbumCardController albumCardController = fxmlLoader.getController();
            albumCardController.setAlbumInfoWithURL(album);
            albumsGrid.add(borderPane, currGridColumn++, currGridRow);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
