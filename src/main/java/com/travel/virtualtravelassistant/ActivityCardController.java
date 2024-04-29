package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.PlanNextTrip.PlanNextTripController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class ActivityCardController {
    private Activity activity;

    private Node node;

    private PlanNextTripController parentController;

    @FXML
    ImageView activityImage;

    @FXML
    Label activityName;

    @FXML
    Label activityLocation;

    @FXML
    Button addButton;

    public void setActivity(Activity activity) {
        this.activity = activity;
        fillActivity();
    }

    public void setNode(Node node){
        this.node = node;
    }

    public void setParentController(PlanNextTripController parentController){
        this.parentController = parentController;
    }

    private void fillActivity(){
        activityName.setText(activity.getName());
        activityLocation.setText(activity.getDescription());
        if(activity.getUrlToFirstImage() != null){
        activityImage.setImage(new Image(activity.getUrlToFirstImage()));
        }
    }

    public void handleAddButton(){
        addButton.setDisable(true);
        addButton.setOpacity(1);
        addButton.setText("Added.");
        parentController.addToSelectedActivities(activity);
    }

    public void handleDeleteButton(){
        GridPane gridPane = (GridPane) node.getParent();
        gridPane.getChildren().remove(node);
    }
}