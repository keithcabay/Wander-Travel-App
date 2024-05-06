package com.travel.virtualtravelassistant.FriendsList;

import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FriendRequestFormController {
    @FXML
    TextField emailField;

    @FXML
    TextField messageField;

    private Stage stage;

    public String getReceiverEmail(){
        return emailField.getText();
    }

    public String getMessage(){
        return messageField.getText();
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void handleSendButton(){
        if (!verifyRequirements()){
            return;
        }
        boolean exist = FirestoreAction.doesUserExist(getReceiverEmail());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if(exist){
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Friend Request sent to: " + getReceiverEmail());
            stage.close();
        }else{
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("User does not exist.");
        }
        alert.showAndWait();
    }

    private boolean verifyRequirements(){
        if(getReceiverEmail().isEmpty()){
            return false;
        }
        return true;
    }
}