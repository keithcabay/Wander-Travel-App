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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class PlanNextTripController implements Initializable {


    @FXML
    private TextField locationTextField;
    @FXML
    private TextArea locationDetailsTextArea;
    @FXML
    private TextArea locationReviewsTextArea;
    @FXML
    private VBox photosContainer;

    private String API_KEY;

    private static final String SEARCH_API_URL = "https://api.content.tripadvisor.com/api/v1/location/search?key=";
    //    private static final String REVIEWS_API_URL = "https://api.tripadvisor.com/data/1.0/location/";
    private static final String DETAILS_API_URL = "https://api.content.tripadvisor.com/api/v1/location/";
    private static final String PHOTOS_API_URL = "https://api.content.tripadvisor.com/api/v1/location/";

    private String readApiKeyFromJsonFile(String jsonFilePath) throws IOException {
        FileInputStream serviceAccountStream = new FileInputStream("src/main/resources/adminSDK.json");
        JsonObject json = JsonParser.parseReader(new InputStreamReader(serviceAccountStream)).getAsJsonObject();
        API_KEY = json.get("trip_advisor_key").getAsString();
        return API_KEY;
    }

    @FXML
    private void searchLocation() throws IOException {
        System.out.println("Searching location...");
        String searchQuery = locationTextField.getText();
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
                    loadLocationDetails(locationId, API_KEY);
//                    loadLocationReviews(locationId, API_KEY);
                    loadLocationPhotos(locationId, API_KEY);
                } else {
                    System.out.println("Error: " + response.code() + " " + response.message());
                    System.out.println(responseBody); // Print the response body string for debugging
                }
            } else {
                throw new IOException("Error: " + response.code());
            }
        }
    }

    private void loadLocationDetails(String locationId, String apiKey) {
        String detailsUrl = DETAILS_API_URL + locationId + "/details?key=" + apiKey + "&language=en&currency=USD";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(detailsUrl)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                JSONObject jsonResponse = new JSONObject(responseBody);

                String name = jsonResponse.getString("name");
                String description = jsonResponse.getString("description");
                locationDetailsTextArea.setText(description);

            } else {
                System.out.println("Unsuccessful response: " + response.code());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadLocationPhotos(String locationId, String apiKey) {
        String photosUrl = PHOTOS_API_URL + locationId + "/photos?key=" + apiKey + "&language=en";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(photosUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

                JsonArray dataArray = jsonResponse.getAsJsonArray("data");

                photosContainer.getChildren().clear();

                for (JsonElement photoElement : dataArray) {
                    JsonObject photoObject = photoElement.getAsJsonObject();
                    JsonObject imagesObject = photoObject.getAsJsonObject("images");

                    JsonObject largeImageObject = imagesObject.getAsJsonObject("large");
                    String photoUrl = largeImageObject.get("url").getAsString();

                    ImageView imageView = new ImageView(new Image(photoUrl));
                    imageView.setFitWidth(300);
                    imageView.setFitHeight(225);

                    photosContainer.getChildren().add(imageView);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            API_KEY = readApiKeyFromJsonFile("src/main/resources/adminSDK.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        locationTextField.setOnKeyPressed(event -> {
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
