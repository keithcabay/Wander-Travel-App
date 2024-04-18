package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.User.CurrentUser;
import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class NavBarController {

    @FXML
    protected Label helloGuest;
    @FXML
    protected Button planTrip;
    @FXML
    protected Button viewTrip;
    @FXML
    protected Button viewPhotos;
    @FXML
    protected Button viewInbox;
    @FXML
    protected Button settings;
    @FXML
    protected Button help;
    @FXML
    protected Button logOut;

    // Method to set the username on the Hello label
    public void setUsername(String username) {
        helloGuest.setText("Hello, " + username + "!");
    }
    public void initialize(){
        helloGuest.setText("Hello, " + CurrentUser.getInstance().getUserInfo().getFirst_name() + "!");
    }

    // Method to switch scenes
    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene scene = new Scene(parent);

            // Get the Stage from the ActionEvent
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onMyProfileClick(ActionEvent event) {
        switchScene(event, "/com/travel/virtualtravelassistant/profilePage.fxml");
    }
    @FXML
    protected void onPlanTripClick(ActionEvent event) {
        switchScene(event, "/com/travel/virtualtravelassistant/PlanNextTrip.fxml");
    }

    @FXML
    protected void onViewTripClick(ActionEvent event) {
        switchScene(event, "/path/to/viewTripView.fxml");
    }

    @FXML
    protected void onViewPhotosClick(ActionEvent event) {
        switchScene(event, "/com/travel/virtualtravelassistant/myPhotos.fxml");
    }

    @FXML
    protected void onViewInboxClick(ActionEvent event) {
        switchScene(event, "/path/to/viewInboxView.fxml");
    }

    @FXML
    protected void onSettingsClick(ActionEvent event) {
        switchScene(event, "/com/travel/virtualtravelassistant/profileSettings.fxml");
    }

    @FXML
    protected void onHelpClick(ActionEvent event) {
        switchScene(event, "/path/to/helpView.fxml");
    }

    @FXML
    protected void onLogOutClick(ActionEvent event) {
        switchScene(event, "/com/travel/virtualtravelassistant/LogIn.fxml");
        CurrentUser.getInstance().setUserInfo(null);
    }
}
