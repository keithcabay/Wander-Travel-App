package com.travel.virtualtravelassistant;

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
import java.util.Map;

public class MyPhotosController {

    @FXML
    GridPane albumsGrid;

    @FXML
    Button saveChanges;

    private int currGridColumn = 0;

    private final int currGridRow = 0;

    private List<Album> newAlbums = new ArrayList<>();

    public void initialize(){
        Map<String, String> albumIdToTitle = FirestoreAction.getAlbums();

        for(String key : albumIdToTitle.keySet()){
            String title = albumIdToTitle.get(key);
            Image image = FirebaseStorageAction.getAlbumImages(key, title);
            if(image != null){
                loadAlbumPreview(new Album(image, title));
            }

        }
    }

    public void handleSaveChangesButton(){
        saveChanges.setVisible(false);
        for(Album album : newAlbums) {
            String albumId = FirestoreAction.storeAlbum(album);
            FirebaseStorageAction.createImageFolder(albumId);
            String httpURL = FirebaseStorageAction.uploadAlbumImage(albumId, album.getTitle(), album.getAlbumCover());
            album.setAlbumCoverURL(httpURL);
        }
        newAlbums = new ArrayList<>();
    }

    public void handleAddAlbumButton(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("createAlbumForm.fxml"));
        CreateAlbumFormController createAlbumFormController;
        try {
            BorderPane borderPane = loader.load();
            createAlbumFormController = loader.getController();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Album Information Form");
            popupStage.setScene(new Scene(borderPane));

            popupStage.showAndWait();

            String image = createAlbumFormController.getImage();
            String title = createAlbumFormController.getTitle();
            Album newAlbum = new Album(image, title);
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
        fxmlLoader.setLocation(getClass().getResource("albumCard.fxml"));

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
    private void loadAlbumPreview(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("albumCard.fxml"));

        try {
            BorderPane borderPane = fxmlLoader.load();
            albumsGrid.add(borderPane, currGridColumn++, currGridRow);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
