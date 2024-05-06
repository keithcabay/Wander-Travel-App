package com.travel.virtualtravelassistant.AlbumControllers;

import com.travel.virtualtravelassistant.DeleteConfirmationPopupController;
import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AlbumCardController {

    Album currAlbum;
    private Node node;

    private UserImage currUserImage;

    private boolean albumCover;

    private Object parent;

    @FXML
    ImageView albumPreviewImage;

    @FXML
    Label title;

    @FXML
    Button viewAlbum;

    @FXML
    Button deleteButton;
    @FXML
    Button settingsButton;
    @FXML
    ImageView coverStar;

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

    public void setNode(Node node) {
        this.node = node;
    }

    public void setParent(Object parent){
        this.parent = parent;
    }

    public void setCurrUserImage(UserImage currUserImage) {
        this.currUserImage = currUserImage;
        if (currAlbum.getAlbumCover().getFirestoreId().equals(currUserImage.getFirestoreId())) {
            albumCover = true;
            Image star = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/travel/virtualtravelassistant/Images/star-icon.png")));
            coverStar.setImage(star);
        }
        Image settings = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/travel/virtualtravelassistant/Images/settings-icon.png")));
        ImageView settingsView = new ImageView(settings);
        settingsView.setFitHeight(25);
        settingsView.setFitWidth(25);
        settingsButton.setGraphic(settingsView);

    }

    public void initialize(){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/travel/virtualtravelassistant/Images/trash-bin.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        deleteButton.setGraphic(imageView);
    }

    public void setSavedFirebase(boolean saved){
        if(saved){
            viewAlbum.setDisable(false);
            deleteButton.setOnAction(e -> handleDeleteButton());
        }
        if(currUserImage != null){
            settingsButton.setVisible(true);
        }
    }

    public void setAlbumInfoWithImage(Album album){
        Image image = new Image("file:///" + album.getLocalPathToCover());
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

    public void handleSettingsButton(){
        openSettingsForm();

    }

    public void handleDeleteButton(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/travel/virtualtravelassistant/deleteConfirmationPopup.fxml"));
        DeleteConfirmationPopupController deleteConfirmationPopupController;
        try {
            VBox vbox = loader.load();
            deleteConfirmationPopupController = loader.getController();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Delete Form");
            popupStage.setScene(new Scene(vbox));
            deleteConfirmationPopupController.setStage(popupStage);
            deleteConfirmationPopupController.setParentController(this);
            if(currUserImage != null){
                deleteConfirmationPopupController.isImage(true);
            }

            popupStage.setOnCloseRequest(Event::consume);
            popupStage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void confirmDelete(){
        if(currUserImage != null) {
            FirebaseStorageAction.deleteImage(currAlbum, currUserImage);
            FirestoreAction.deleteImage(currAlbum, currUserImage);
        }else{
            FirebaseStorageAction.deleteAlbum(currAlbum);
            FirestoreAction.deleteAlbum(currAlbum);
        }
        GridPane gridPane = (GridPane) node.getParent();
        gridPane.getChildren().remove(node);
    }

    public void deleteSelf(){
        if(parent instanceof ImagesOfAlbumController){
            ((ImagesOfAlbumController) parent).deleteImageFromNew(currUserImage);
        }else if(parent instanceof MyPhotosController){
            ((MyPhotosController) parent).deleteAlbumFromNew(currAlbum);
        }
        GridPane gridPane = (GridPane) node.getParent();
        gridPane.getChildren().remove(node);

    }

    private void openSettingsForm(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/travel/virtualtravelassistant/createAlbumForm.fxml"));
        CreateAlbumFormController createAlbumFormController;
        try {
            BorderPane borderPane = loader.load();
            createAlbumFormController = loader.getController();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Settings");
            popupStage.setScene(new Scene(borderPane));
            createAlbumFormController.setStage(popupStage);
            createAlbumFormController.setSettingsForm(true);
            createAlbumFormController.setAlbumCover(albumCover);
            popupStage.showAndWait();

            boolean canceled = createAlbumFormController.getCancelled();
            if(canceled){
                return;
            }

            String caption = createAlbumFormController.getCaption();
            boolean checked = createAlbumFormController.getCoverChecked();
            if(checked){
                currAlbum.setAlbumCover(currUserImage);
            }

            title.setText(caption);
            currUserImage.setCaption(caption);
            FirestoreAction.updateImage(currAlbum, currUserImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

