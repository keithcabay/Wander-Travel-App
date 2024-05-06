package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.AlbumControllers.AlbumCardController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class DeleteConfirmationPopupController {
    @FXML
    Label deletePromptLabel;
    @FXML
    Button deleteButton;
    @FXML
    Button cancelButton;

    private Object parentController;
    private Stage stage;
    private boolean image;

    public void isImage(boolean isImage){
        image = isImage;
        deletePromptLabel.setText("Are you sure you want to delete this image? You will not be able to undo this deletion as it is permanent.");
    }
    public void handleFirstDeleteButton(){
        System.out.println("First delete");
        if(image){
            deletePromptLabel.setText("This is your last chance. If you are sure you want to delete this image press the delete button below.");
        }else{
            deletePromptLabel.setText("This is your last chance. If you are sure you want to delete this album and its images press the delete button below.");
        }
        Button newCancel = cancelButton;
        HBox parent = (HBox) deleteButton.getParent();

        // Get the indexes of the buttons in the parent
        int deleteIndex = parent.getChildren().indexOf(deleteButton);
        int cancelIndex = parent.getChildren().indexOf(cancelButton);

        // Remove both buttons from the parent
        parent.getChildren().removeAll(deleteButton, cancelButton);

        // Add the buttons back in reversed order
        parent.getChildren().add(cancelIndex, deleteButton);
        parent.getChildren().add(deleteIndex, cancelButton);
        deleteButton.setOnAction(e -> handleSecondDeleteButton());
    }

    public void setParentController(Object parentController) {
        this.parentController = parentController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void handleSecondDeleteButton(){
        System.out.println("2nd delete");
        if(parentController instanceof AlbumCardController){
            ((AlbumCardController) parentController).confirmDelete();
        }
        stage.close();
    }

    public void handleCancelButton(){
        System.out.println("Cancel");
        stage.close();
    }
}
