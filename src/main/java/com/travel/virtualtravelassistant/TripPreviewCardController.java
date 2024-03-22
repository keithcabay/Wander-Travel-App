package com.travel.virtualtravelassistant;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TripPreviewCardController {
    @FXML
    private ImageView imageOfDestination;

    @FXML
    private Label destinationName;

    @FXML
    private Label tripBudget;

    @FXML
    private Label tripLength;

    public void setTrip(Trip trip){
        Image image = new Image(getClass().getResourceAsStream(trip.getImageSource()));
        imageOfDestination.setImage(image);
        imageOfDestination.setPreserveRatio(false);
        imageOfDestination.setOpacity(0.65);
        imageOfDestination.setFitWidth(550);
        destinationName.setText(trip.getCityName() + ", " + trip.getCountryName());
        tripBudget.setText("$" + trip.getBudget());
        tripLength.setText(trip.getLength() + " Days");
    }
}