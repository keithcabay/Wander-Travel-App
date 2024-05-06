package com.travel.virtualtravelassistant.AlbumControllers;

import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPhotosController {
    @FXML
    GridPane albumsGrid;
    @FXML
    Button saveChanges;
    private int currGridColumn = 0;
    private final int currGridRow = 0;
    private Map<Album, AlbumCardController> newAlbums;


    /***
     * On page load albums information will be fetched from Firestore and loaded from Firebase storage
     */
    public void initialize(){
        newAlbums = new HashMap<>();
        List<Album> albums = FirestoreAction.getAlbums();

        for(Album album : albums){
            loadAlbumPreviewWithDB(album);
        }
    }


    /***
     * Method for when save change button is clicked.
     * Saves all the albums in newAlbums to Firebase Storage and Firestore.
     */
    public void handleSaveChangesButton(){
        saveChanges.setVisible(false);

        for(Map.Entry<Album, AlbumCardController> entry : newAlbums.entrySet()) {
            Album album = entry.getKey();
            String httpURL = FirebaseStorageAction.uploadImageToAlbum(album, album.getAlbumCover());
            album.getAlbumCover().setImageURL(httpURL);

            FirestoreAction.storeAlbumInfo(album);
            entry.getValue().setSavedFirebase(true);
        }
        newAlbums = new HashMap<>();
    }


    /***
     * Method for when add album button is clicked.
     * Gets album created locally and adds it to the newAlbums list for remote processing.
     */
    public void handleAddAlbumButton(){
        Album newAlbum = createAlbumForm();
        if(newAlbum != null) {
            AlbumCardController controller = loadAlbumPreview(newAlbum);
            newAlbums.put(newAlbum, controller);
            saveChanges.setVisible(true);
        }

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
            createAlbumFormController.setStage(popupStage);

            popupStage.showAndWait();

            boolean canceled = createAlbumFormController.getCancelled();
            if(canceled){
                return null;
            }

            String pathToImage = createAlbumFormController.getImage();
            String title = createAlbumFormController.getTitle();
            String caption = createAlbumFormController.getCaption();

            if(title == null){
                return null;
            }

            UserImage userImage = new UserImage();
            userImage.setLocalPathToImage(pathToImage);
            userImage.setCaption(caption);

            Album newAlbum = new Album();
            newAlbum.setLocalPathToCover(userImage.getLocalPathToImage());
            newAlbum.setTitle(title);
            newAlbum.setAlbumCover(userImage);
            newAlbum.addImage(userImage);

            return  newAlbum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAlbumFromNew(Album album){
        newAlbums.remove(album);
        currGridColumn--;
    }

    //load album preview card with given album information
    private AlbumCardController loadAlbumPreview(Album album){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/albumCard.fxml"));

        try {
            BorderPane borderPane = fxmlLoader.load();
            AlbumCardController albumCardController = fxmlLoader.getController();
            albumCardController.setCurrAlbum(album);
            albumCardController.setAlbumInfoWithImage(album);
            albumCardController.setNode(borderPane);
            albumCardController.setParent(this);
            albumsGrid.add(borderPane, currGridColumn++, currGridRow);
            return albumCardController;
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
            albumCardController.setCurrAlbum(album);
            albumCardController.setAlbumInfoWithURL(album);
            albumCardController.setNode(borderPane);
            albumCardController.setSavedFirebase(true);
            albumCardController.setParent(this);
            albumsGrid.add(borderPane, currGridColumn++, currGridRow);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

