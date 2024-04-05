package com.travel.virtualtravelassistant.PlanNextTrip;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class PlanNextTripController implements Initializable {

    @FXML
    private WebView webView;
    @FXML
    private TextField locationTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization code here
    }

    @FXML
    private void searchLocation() {

        String apiKey = "2175F302BAC646F9991CD7DF16E2ECA6";
        String locationId = "19526673";  // locationTextField.getText(); // Get the location input from the text field
        String apiUrl = "https://api.tripadvisor.com/data/1.0/location/" + locationId + "/reviews?key=" + apiKey;

        WebEngine webEngine = webView.getEngine();
        webEngine.load(apiUrl);
    }




}

