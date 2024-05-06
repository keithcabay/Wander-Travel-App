package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.TripInfo.Trip;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LargeTripPreviewBannerController {
    @FXML
    Label titleLabel;
    @FXML
    ImageView imageView;
    @FXML
    ScrollPane scrollPane;

    public void setTrip(Trip trip) {
       titleLabel.setText(trip.getTitle());
        System.out.println("IN TRIP URL" + trip.getUrlToFirstImage());
       if(trip.getUrlToFirstImage() != null) {
           imageView.setOpacity(0.45);
           Image image = new Image(trip.getUrlToFirstImage());
           imageView.setImage(image);
           imageView.setPreserveRatio(true);
           double scale = 1200.0 / image.getWidth();
           imageView.setFitWidth(1200);
           imageView.setFitHeight(image.getHeight() * scale);


       }
    }
}
