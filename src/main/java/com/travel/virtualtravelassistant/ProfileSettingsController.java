package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.User.CurrentUser;
import com.travel.virtualtravelassistant.Utility.ApplicationUtil;
import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ProfileSettingsController {

    @FXML
    ImageView imageView;

    @FXML
    Label firstName;

    @FXML
    Label lastName;

    @FXML
    Label Bio;

    @FXML
    Label locationLabel;

    @FXML
    TextArea bioField;

    @FXML
    TextField firstNameField;

    @FXML
    TextField lastNameField;

    @FXML
    TextField locationField;

    @FXML
    Button saveChangesButton;

    @FXML
    Button discardChangesButton;

    public void initialize(){
        Image profile_pic = FirebaseStorageAction.getProfilePicture();
        ApplicationUtil.setImageViewRoundCroppedProfilePic(imageView, profile_pic);

        String firsNameText = CurrentUser.getInstance().getUserInfo().getFirst_name();
        String lastNameText = CurrentUser.getInstance().getUserInfo().getLast_name();
        String locationText = CurrentUser.getInstance().getUserInfo().getLocation();
        String bioText = CurrentUser.getInstance().getUserInfo().getBio();

        firstName.setText(firsNameText);
        lastName.setText(lastNameText);
        firstNameField.setText(firsNameText);
        lastNameField.setText(lastNameText);

        if(CurrentUser.getInstance().getUserInfo().getBio() != null){
            Bio.setText(bioText);
            bioField.setText(bioText);
        }

        if(CurrentUser.getInstance().getUserInfo().getLocation() != null){
            locationLabel.setText(locationText);
            locationField.setText(locationText);
        }

        Bio.textProperty().bindBidirectional(bioField.textProperty());
        firstName.textProperty().bindBidirectional(firstNameField.textProperty());
        lastName.textProperty().bindBidirectional(lastNameField.textProperty());
        locationLabel.textProperty().bindBidirectional(locationField.textProperty());
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

    public void onSaveChangesClick(ActionEvent event){
        String firsNameText = firstName.getText();
        String lastNameText = lastName.getText();
        String locationText = locationLabel.getText();
        String bioText = Bio.getText();

        CurrentUser.getInstance().getUserInfo().setFirst_name(firsNameText);
        CurrentUser.getInstance().getUserInfo().setLast_name(lastNameText);
        CurrentUser.getInstance().getUserInfo().setLocation(locationText);
        CurrentUser.getInstance().getUserInfo().setBio(bioText);

        FirestoreAction.saveProfileChanges(firsNameText, lastNameText, locationText, bioText);
        onEditProfileClick(event);
    }

    public void onDiscardChangesClick(){
        String firsNameText = CurrentUser.getInstance().getUserInfo().getFirst_name();
        String lastNameText = CurrentUser.getInstance().getUserInfo().getLast_name();
        String locationText = CurrentUser.getInstance().getUserInfo().getLocation();
        String bioText = CurrentUser.getInstance().getUserInfo().getBio();

        firstName.setText(firsNameText);
        lastName.setText(lastNameText);

        locationLabel.setText(Objects.requireNonNullElse(locationText, ""));

        Bio.setText(Objects.requireNonNullElse(bioText, ""));
    }

    private void onEditProfileClick(ActionEvent event){
        try {
            Parent parent = FXMLLoader.load((getClass().getResource("profileSettings.fxml")));
            Scene scene = new Scene(parent);

            // Get the Stage from the ActionEvent
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load " + "profileSettings.fxml" +  " page.");
        }
    }
}
