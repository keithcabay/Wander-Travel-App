package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class CreateAlbumFormController {
    @FXML
    TextField titleField;
    @FXML
    Button selection;

    private String title;
    private String image;

    public String getTitle(){
        return title;
    }

    public String getImage(){
        return image;
    }

    public void handleChooseImageButton(){
        title = titleField.getText();
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
            System.out.println(image);
            selection.setDisable(true);
            selection.setText("Photo Selected.");
        }else{
            System.out.println("No file selected.");
        }
    }
}
