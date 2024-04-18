package com.travel.virtualtravelassistant.TripInfo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class TripPreviewCardController {
    @FXML
    private ImageView imageOfDestination;

    @FXML
    private Label destinationName;

    @FXML
    private Label tripBudget;

    @FXML
    private Label tripLength;

    public void setUpcomingTrip(Trip trip){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(trip.getImageSource())));
        imageOfDestination.setImage(image);
        imageOfDestination.setPreserveRatio(false);
        imageOfDestination.setOpacity(0.65);
        imageOfDestination.setFitWidth(550);
        destinationName.setText(trip.getCityName() + ", " + trip.getCountryName());
        tripBudget.setText("$" + trip.getBudget());
        tripLength.setText(trip.getLength() + " Days");
    }

    public void setPreviousTrip(Trip trip){
        Image image = trip.getImage();
        imageOfDestination.setImage(image);
        imageOfDestination.setPreserveRatio(false);
        imageOfDestination.setOpacity(0.65);
        imageOfDestination.setFitWidth(550);
        destinationName.setText(trip.getCountryName());
    }
}
