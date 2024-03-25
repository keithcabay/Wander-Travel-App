package com.travel.virtualtravelassistant;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
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
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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
            goToPage(event, "homeView.fxml");
        }

    }

    @FXML
    protected void onCreateAccountButtonClicked(ActionEvent event){
        goToPage(event, "registrationPage.fxml");
    }

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

            if(password.equals(validPassword)){
                UserInfo user = new UserInfo(userFirstName, userLastName, email);
                CurrentUser.getInstance().setUserInfo(user);
                return true;
            }else{
                errorText.setVisible(true);
                return false;
            }

        } catch (FirebaseAuthException e) {
            System.out.println("Failed to find user.");
        }

        return false;
    }

    private void goToPage(ActionEvent event, String fxml){
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
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
