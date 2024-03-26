package com.travel.virtualtravelassistant.AuthenticationControllers;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuthException;
import com.travel.virtualtravelassistant.MainApplication;
import com.travel.virtualtravelassistant.User.CurrentUser;
import com.travel.virtualtravelassistant.User.UserInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
public class LogInController {
    @FXML
    private Label errorText;

    @FXML
    TextField email;

    @FXML
    PasswordField password;

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String userEmail = email.getText();
        String userPassword = password.getText();

        boolean authenticated = validateUser(userEmail, userPassword);

        if (authenticated) {
            goToPage(event, "/com/travel/virtualtravelassistant/homeView.fxml");
        }

    }
    // redirects to registration page
    @FXML
    protected void onCreateAccountButtonClicked(ActionEvent event){
        goToPage(event, "/com/travel/virtualtravelassistant/registrationPage.fxml");
    }
    // method to validate user with email and password for now
    // later on can add other authentication methods
    private boolean validateUser(String email, String password){
        try {
            String UID = MainApplication.fauth.getUserByEmail(email).getUid();

            ApiFuture<DocumentSnapshot> future = MainApplication.fstore.collection("Users").document(UID).get();

            String validPassword = null;
            String userFirstName = null;
            String userLastName = null;

            try{
                DocumentSnapshot snap = future.get();
                validPassword = snap.getString("password");
                userFirstName = snap.getString("first_name");
                userLastName = snap.getString("last_name");
            }catch(Exception e){
                System.out.println("Error getting document.");
            }
            // checks password
            if(password.equals(validPassword)){
                UserInfo user = new UserInfo(UID, userFirstName, userLastName, email);
                CurrentUser.getInstance().setUserInfo(user);
                return true;
            }else{
                errorText.setVisible(true);
                return false;
            }
        // runs if user can not be authenticated and displays an error message
        } catch (FirebaseAuthException e) {
            System.out.println("Failed to find user.");
            errorText.setVisible(true);
        }

        return false;
    }
    // goes to page similar to other controller classes
    private void goToPage(ActionEvent event, String fxml){
        try {
            Parent parent = FXMLLoader.load((getClass().getResource(fxml)));
            Scene scene = new Scene(parent);

            // Get the Stage from the ActionEvent
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load " + fxml +  " page.");
        }
    }
}
