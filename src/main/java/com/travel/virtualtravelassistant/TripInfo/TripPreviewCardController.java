package com.travel.virtualtravelassistant.TripInfo;

import com.travel.virtualtravelassistant.FinalTripPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class TripPreviewCardController {
    @FXML
    private ImageView imageOfDestination;

    @FXML
    private Label destinationName;

    @FXML
    private Label tripBudget;

    @FXML
    private Label tripLength;
    @FXML
    Button viewTripButton;
    private Trip trip;

    public void setUpcomingTrip(Trip trip){
        this.trip = trip;
        viewTripButton.setVisible(true);
        imageOfDestination.setImage(trip.getImage());
        imageOfDestination.setPreserveRatio(false);
        imageOfDestination.setOpacity(0.65);
        imageOfDestination.setFitWidth(550);
        destinationName.setText(trip.getTitle());
        if(trip.getBudget() != null) {
            tripBudget.setText("$" + trip.getBudget());
        }

        if(trip.getLength() != null) {
            tripLength.setText(trip.getLength() + " Days");
        }
    }

    public void setPreviousTrip(Trip trip){
        this.trip = trip;
        Image image = trip.getImage();
        imageOfDestination.setImage(image);
        imageOfDestination.setPreserveRatio(false);
        imageOfDestination.setOpacity(0.65);
        imageOfDestination.setFitWidth(550);
        destinationName.setText(trip.getTitle());
    }

    public void handleViewTripsButton(ActionEvent event){
        loadFinalTripPage(event);
    }

    private void loadFinalTripPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/travel/virtualtravelassistant/finalTripPage.fxml"));
            Parent parent = loader.load();
            FinalTripPageController finalTripPageController = loader.getController();
            finalTripPageController.loadTripFromDB(trip);
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}