package com.travel.virtualtravelassistant;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class LogInController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onLoginButtonClick() {
        try {
            MainApplication.showHomeView();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception as appropriate
        }
    }
}
