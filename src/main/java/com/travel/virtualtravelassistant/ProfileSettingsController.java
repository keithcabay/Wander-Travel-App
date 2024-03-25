package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.User.CurrentUser;
import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ProfileSettingsController {
    
    @FXML
    ImageView imageView;

    @FXML
    Label userName;

    @FXML
    Label Bio;

    @FXML
    TextArea TextArea;

    public void initialize(){
        System.out.println("Initializing Profile Settings page.");
        userName.setText(CurrentUser.getInstance().getUserInfo().getFullName());
        Image image = FirebaseStorageAction.getProfilePicture();
        System.out.println(image);
        imageView.setImage(image);
        Bio.textProperty().bind(TextArea.textProperty());
    }

    public void onUploadProfilePictureClick(){
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File fileSelected = fileChooser.showOpenDialog(new Stage());

        if(fileSelected != null){
            FirebaseStorageAction.uploadProfilePicture(fileSelected);
            Image image = new Image(fileSelected.toURI().toString());
            imageView.setImage(image);
        }else{
            System.out.println("No file selected.");
        }

    }
}
