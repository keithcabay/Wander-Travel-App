package com.travel.virtualtravelassistant.AlbumControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class CreateAlbumFormController {
    @FXML
    Label titleLabel;
    @FXML
    TextField titleField;
    @FXML
    Label captionLabel;
    @FXML
    TextField captionField;
    @FXML
    Label selectImageTitleLabel;
    @FXML
    Button selection;
    @FXML
    CheckBox coverImageCheckBox;

    private String title;
    private String caption;
    private String image;
    private Stage stage;
    private boolean cancelled;

    public void initialize(){
        cancelled = false;
    }

    public String getTitle(){
        return title;
    }

    public String getCaption(){
        return caption;
    }

    public boolean getCoverChecked(){
        return coverImageCheckBox.isSelected();
    }

    public String getImage(){
        return image;
    }

    public boolean getCancelled(){
        return cancelled;
    }

    public void setAlbumCover(boolean albumCover){
        if(albumCover){
            coverImageCheckBox.setSelected(true);
        }
    }

    public void setImageForm(boolean imageForm){
        if(imageForm) {
            titleLabel.setManaged(false);
            titleField.setManaged(false);
            selectImageTitleLabel.setText("Select Image to Add...");
        }
    }

    public void setSettingsForm(boolean settingsForm){
        if(settingsForm){
            coverImageCheckBox.setVisible(true);
            captionLabel.setText("Change image caption...");
            titleLabel.setManaged(false);
            titleField.setManaged(false);
            selection.setManaged(false);
            selectImageTitleLabel.setManaged(false);
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void handleChooseImageButton(){
        title = titleField.getText();
        caption = captionField.getText();
        openFileChooser();
    }

    private void openFileChooser(){
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File fileSelected = fileChooser.showOpenDialog(new Stage());

        if(fileSelected != null){
            image = fileSelected.getAbsolutePath();
            selection.setDisable(true);
            selection.setText("Photo Selected.");
        }else{
            System.out.println("No file selected.");
        }
    }

    public void handleSaveButton(){
        caption = captionField.getText();
        cancelled = false;
        stage.close();
    }

    public void handleCancelButton(){
        cancelled = true;
        stage.close();
    }
}
