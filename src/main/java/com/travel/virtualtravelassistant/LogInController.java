package com.travel.virtualtravelassistant;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LogInController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}