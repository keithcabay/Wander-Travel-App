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

    public void initialize(){
        List<Album> albums = FirestoreAction.getAlbums();

        for(Album album : albums){
            Image image = new Image(album.getAlbumCover().getImageURL());
            album.setCurrImage(image);
            loadAlbumPreviewWithDB(album);
        }
    }

    public void handleSaveChangesButton(){
        saveChanges.setVisible(false);

        for(Album album : newAlbums) {
            String albumId = FirebaseStorageAction.createImageFolder();
            String httpURL = FirebaseStorageAction.uploadAlbumImage(albumId, album.getPathToLocalCover());
            album.setFirestoreId(albumId);
            UserImage userImage = new UserImage();
            userImage.setImageURL(httpURL);
            album.setAlbumCover(userImage);
            FirestoreAction.storeAlbumInfo(album);
        }
        newAlbums = new ArrayList<>();
    }

    public void handleAddAlbumButton(){
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

            Album newAlbum = new Album();
            newAlbum.setPathToLocalCover(pathToImage);
            newAlbum.setTitle(title);

            newAlbums.add(newAlbum);

            loadAlbumPreview(newAlbum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        saveChanges.setVisible(true);
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
