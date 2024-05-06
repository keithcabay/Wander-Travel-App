package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.PlanNextTrip.PlanNextTripController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.net.URL;

public class ActivityCardController {
    private Activity activity;

    private Node node;

    private int day;

    private PlanNextTripController planNextTripController;

    private BuildItineraryController buildItineraryController;

    @FXML
    ImageView activityImage;

    @FXML
    Label activityName;

    @FXML
    Label activityLocation;

    @FXML
    Button largeCardButton;

    @FXML
    Button smallCardButton;

    @FXML
    Label descriptionLabel;

    @FXML
    Button moreInfoButton;
    @FXML
    Button loadNearbyButton;

    public void setActivity(Activity activity) {
        this.activity = activity;
        fillActivity();
    }

    public void setDay(int day){
        this.day = day;
    }

    public int getDay(){
        return day;
    }

    public void setNode(Node node){
        this.node = node;
    }

    public void setPlanNextTripParentController(PlanNextTripController parentController){
        this.planNextTripController = parentController;
    }

    public void setBuildItineraryParentController(BuildItineraryController buildItineraryController){
        if(loadNearbyButton != null) {
            loadNearbyButton.setManaged(false);
        }
        this.buildItineraryController = buildItineraryController;
        if(smallCardButton != null) {
            smallCardButton.setText("Add");
            smallCardButton.setOnAction(e -> handleSmallActivityAddButton());
            smallCardButton.setPrefWidth(90);
        }
        if(largeCardButton != null) {
            largeCardButton.setText("Remove");
            largeCardButton.setOnAction(e -> handleLargeActivityDeleteButton());
        }
    }

    private void fillActivity(){
        activityName.setText(activity.getName());
        activityLocation.setText(activity.getCity() + ", " + activity.getCountry());

        if(largeCardButton != null) {
            descriptionLabel.setText(activity.getDescription());
            moreInfoButton.setVisible(true);
        }


    }

    public void handleAddButton(){
        largeCardButton.setDisable(true);
        largeCardButton.setOpacity(1);
        largeCardButton.setText("Added.");
        planNextTripController.addToSelectedActivities(activity);
    }

    public void handleDeleteButton(){
        planNextTripController.removeFromSelectedActivities(activity);
        GridPane gridPane = (GridPane) node.getParent();
        gridPane.getChildren().remove(node);
    }

    public void handleSmallActivityAddButton(){
        if(buildItineraryController.addToTrip(activity)) {
            GridPane gridPane = (GridPane) node.getParent();
            gridPane.getChildren().remove(node);
        }
    }

    public void handleLargeActivityDeleteButton(){
        buildItineraryController.removeFromTrip(activity, day);
        GridPane gridPane = (GridPane) node.getParent();
        gridPane.getChildren().remove(node);
    }

    public void handleMoreInfoButton(){
        if(planNextTripController != null){
            planNextTripController.loadLocationDetails(activity);
        }
        try {
            System.out.println(activity.getUrlMoreInfo());
            Desktop.getDesktop().browse(new URL(activity.getUrlMoreInfo()).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleLoadNearby(){
        planNextTripController.searchNearbyLocationActivity(activity);
    }

    public void setFinalTrip(boolean isFinalTrip) {
        if(isFinalTrip){
            loadNearbyButton.setManaged(false);
            largeCardButton.setManaged(false);
        }
    }
}