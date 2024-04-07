package com.travel.virtualtravelassistant.PlanNextTrip;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlanNextTripController implements Initializable {

    @FXML
    private WebView tripAdvisorWebView;
    @FXML
    private TextArea locationTextArea;

    private static final String API_KEY = "2175F302BAC646F9991CD7DF16E2ECA6";
    private static final String SEARCH_API_URL = "https://api.content.tripadvisor.com/api/v1/location/search?key=";
    private static final String REVIEWS_API_URL = "https://api.tripadvisor.com/data/1.0/location/";
    private static final String DETAILS_API_URL = "https://api.content.tripadvisor.com/api/v1/location/";
    private static final String PHOTOS_API_URL = "https://api.content.tripadvisor.com/api/v1/location/";

    @FXML
    private void searchLocation() throws IOException {
        System.out.println("Searching location...");
        String searchQuery = locationTextArea.getText();
        OkHttpClient client = new OkHttpClient();
        String apiUrl = SEARCH_API_URL + API_KEY +
                "&searchQuery=" + searchQuery +
                "&category=geos" +  // Add category parameter for geos search
                "&language=en"; // Add language parameter for English results

        String responseBody;
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                responseBody = response.body().string(); // Store the response body string
                System.out.println("Request successful:");
                System.out.println(responseBody);
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonArray locations = jsonResponse.getAsJsonArray("data");
                if (locations != null && locations.size() > 0) {
                    JsonObject firstLocation = locations.get(0).getAsJsonObject();
                    String locationId = firstLocation.get("location_id").getAsString();
                    System.out.println("Location ID: " + locationId);
                    loadLocationDetails(locationId);
                    loadLocationReviews(locationId);
                    loadLocationPhotos(locationId);
                } else {
                    System.out.println("Error: " + response.code() + " " + response.message());
                    System.out.println(responseBody); // Print the response body string for debugging
                }
            } else {
                throw new IOException("Error: " + response.code());
            }
        }
    }

    private void loadLocationDetails(String locationId) {
        String detailsUrl = DETAILS_API_URL + locationId + "/details?key=" + API_KEY + "&language=en&currency=USD";
        WebEngine webEngine = tripAdvisorWebView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                System.out.println("Location Details Loaded:");
                System.out.println(webEngine.getDocument().toString());
            }
        });
        webEngine.load(detailsUrl);
    }

    private void loadLocationReviews(String locationId) {
        String reviewsUrl = REVIEWS_API_URL + locationId + "/reviews?key=" + API_KEY;
        WebEngine webEngine = tripAdvisorWebView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                System.out.println("Location Reviews Loaded:");
                System.out.println(webEngine.getDocument().toString());
            }
        });
        webEngine.load(reviewsUrl);
    }

    private void loadLocationPhotos(String locationId) {
        String photosUrl = PHOTOS_API_URL + locationId + "/photos?key=" + API_KEY + "&language=en";
        WebEngine webEngine = tripAdvisorWebView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                System.out.println("Location Photos Loaded:");
                System.out.println(webEngine.getDocument().toString());
            }
        });
        webEngine.load(photosUrl);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        locationTextArea.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    searchLocation();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}