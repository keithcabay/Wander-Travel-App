package com.travel.virtualtravelassistant;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class HomeController {

    @FXML
    private Label destinationLabel;
    @FXML
    private Button planTripButton;
    @FXML
    private Button exploreSuggestionsButton;
    @FXML
    private ImageView destinationImageView;
    @FXML
    private ImageView suggestionsImageView;


    @FXML
    private void planTrip() {
        // Implement your logic for planning a trip
        System.out.println("Planning a new trip...");
    }

    @FXML
    private void exploreSuggestions() {
        // Implement your logic for exploring suggestions
        System.out.println("Exploring suggestions...");
    }

    @FXML
    protected void handleCloseCLick(){
        Platform.exit();
        System.out.println("Exit CLicked");
    }
}