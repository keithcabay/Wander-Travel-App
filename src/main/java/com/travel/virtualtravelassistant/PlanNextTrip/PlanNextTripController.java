package com.travel.virtualtravelassistant.PlanNextTrip;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.travel.virtualtravelassistant.Activity;
import com.travel.virtualtravelassistant.ActivityCardController;
import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlanNextTripController implements Initializable {

    @FXML
    private ImageView profilePicImage;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextArea locationDetailsTextArea;
    @FXML
    private HBox photosContainer;
    @FXML
    private Slider foodSlider;
    @FXML
    private TextField foodTextField;
    @FXML
    private Slider roomAndBoardSlider;
    @FXML
    private TextField roomAndBoardTextField;
    @FXML
    private Slider spendingSlider;
    @FXML
    private TextField spendingTextField;
    @FXML
    private TextField totalBudgetTextField;
    @FXML
    private TextField vacationLengthTextField;
    @FXML
    private TextArea attractionsTextArea;
    @FXML
    private TextArea reviewsTextArea;
    @FXML
    private Button continueButton;
    @FXML
    private GridPane attractionsGrid;
    @FXML
    private GridPane hotelsGrid;
    @FXML
    private GridPane reviewsGrid;
    @FXML
    private GridPane addedGrid;

    private int currGridColumn = 0;
    private  int currGridRow = 0;



    private double totalBudget = 0;
    private int vacationLength = 0;
    private double dailyBudget = 0;
    private String API_KEY;

    private static final String SEARCH_API_URL = "https://api.content.tripadvisor.com/api/v1/location/search?key=";
    private static final String REVIEWS_API_URL = "https://api.content.tripadvisor.com/api/v1/location/";
    private static final String DETAILS_API_URL = "https://api.content.tripadvisor.com/api/v1/location/";
    private static final String PHOTOS_API_URL = "https://api.content.tripadvisor.com/api/v1/location/";
    private static final String REVIEWS_API_PHOTOS_URL = "https://api.content.tripadvisor.com/api/v1/location/";
    private static final String ATTRACTIONS_API_URL = "https://api.content.tripadvisor.com/api/v1/location/search?key=";


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

        String apiUrl = SEARCH_API_URL + API_KEY + "&searchQuery=" + searchQuery +
                //"&category=geos" +  // Add category parameter for geos search
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
                    loadLocationReviews(locationId, API_KEY);
                    loadLocationAndReviewPhotos(locationId, API_KEY);
                    loadLocationAttractions(locationId, API_KEY);
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

    private void loadLocationAndReviewPhotos(String locationId, String apiKey) {
        String photosUrl = PHOTOS_API_URL + locationId + "/photos?key=" + apiKey + "&language=en";
        String reviewsUrl = REVIEWS_API_PHOTOS_URL + locationId + "/reviews?key=" + apiKey + "&language=en";

        OkHttpClient client = new OkHttpClient();
        photosContainer.getChildren().clear();  // Clear previous photos

        // Load location photos
        Request photosRequest = new Request.Builder()
                .url(photosUrl)
                .get()
                .addHeader("accept", "application/json")
                .build();

        client.newCall(photosRequest).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray photosArray = jsonResponse.getJSONArray("data");

                    Platform.runLater(() -> {
                        try {
                            for (int i = 0; i < photosArray.length(); i++) {
                                JSONObject photoObject = photosArray.getJSONObject(i);
                                JSONObject imagesObject = photoObject.getJSONObject("images");
                                JSONObject largeImageObject = imagesObject.getJSONObject("large");
                                String photoUrl = largeImageObject.getString("url");

                                ImageView imageView = new ImageView(new Image(photoUrl));
                                imageView.setFitWidth(300);
                                imageView.setFitHeight(225);
                                photosContainer.getChildren().add(imageView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    System.out.println("Failed to fetch location photos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });

        // Load review photos
        Request reviewsRequest = new Request.Builder()
                .url(reviewsUrl)
                .get()
                .addHeader("accept", "application/json")
                .build();

        client.newCall(reviewsRequest).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray reviewsArray = jsonResponse.getJSONArray("data");

                    Platform.runLater(() -> {
                        try {
                            for (int i = 0; i < reviewsArray.length(); i++) {
                                JSONObject review = reviewsArray.getJSONObject(i);
                                if (review.has("user") && review.getJSONObject("user").has("avatar")) {
                                    JSONObject avatar = review.getJSONObject("user").getJSONObject("avatar");
                                    String photoUrl = avatar.optString("large", "");

                                    if (!photoUrl.isEmpty()) {
                                        ImageView imageView = new ImageView(new Image(photoUrl, true));
                                        imageView.setPreserveRatio(true);
                                        imageView.setFitHeight(220); // Set max height
                                        photosContainer.getChildren().add(imageView);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    System.out.println("Failed to fetch review photos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadLocationReviews(String locationId, String API_KEY) {
        String reviewsUrl = REVIEWS_API_URL + locationId + "/reviews?key=" + API_KEY + "&language=en";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(reviewsUrl)
                .get()
                .addHeader("accept", "application/json")
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    System.out.println("Raw response: " + responseBody);  // Print the raw response to the console
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray reviewsArray = jsonResponse.getJSONArray("data");

                    StringBuilder reviewsContent = new StringBuilder();

                    System.out.println("Reviews for location ID: " + locationId); // Print location ID to console
                    for (int i = 0; i < reviewsArray.length(); i++) {
                        JSONObject review = reviewsArray.getJSONObject(i);
                        String title = review.optString("title", "No Title");
                        String text = review.optString("text", "No review text available.");

                        // Build the string for TextArea
                        reviewsContent.append("Review #").append(i + 1).append(": ").append(title).append("\n");
                        reviewsContent.append("Text: ").append(text).append("\n");
                        reviewsContent.append("-------------------------------\n");

                        // Print each review to the console
                        System.out.println("Review #" + (i + 1) + ": " + title);
                        System.out.println("Text: " + text);
                        System.out.println("-------------------------------");
                    }

                    String finalText = reviewsContent.toString();
                    Platform.runLater(() -> {
                        reviewsTextArea.setText(finalText); // Update the TextArea on the JavaFX thread
                    });
                } else {
                    System.err.println("Failed to fetch reviews: " + response.code());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }




    //Location Attractions
    private void loadLocationAttractions(String locationId, String apiKey) {

        String attractionsUrl = ATTRACTIONS_API_URL + apiKey + "&searchQuery=" + locationId + "&category=attraction" + "&language=en&currency=USD";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(attractionsUrl)
                .get()
                .addHeader("accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONArray locations = jsonResponse.getJSONArray("data");

                StringBuilder attractionsDetails = new StringBuilder();
                for (int i = 0; i < locations.length(); i++) {
                    JSONObject location = locations.getJSONObject(i);
                    String name = location.getString("name");
                    JSONObject addressObj = location.getJSONObject("address_obj");
                    String addressString = addressObj.getString("address_string");

                    attractionsDetails.append(name).append(" - ").append(addressString).append("\n");
                }

                
                attractionsTextArea.setText(attractionsDetails.toString());

            } else {
                System.out.println("Unsuccessful response: " + response.code() + " - " + response.message());
            }
        } catch (IOException | JSONException e) {
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

        setupFieldListeners();
        setupSliderListeners();
        setupTextFieldListeners();

        profilePicImage.setImage(FirebaseStorageAction.getProfilePicture());

        locationTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    searchLocation();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Set listeners for total budget and vacation length text fields
        totalBudgetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                totalBudget = Double.parseDouble(newValue);

            }
        });

        vacationLengthTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                vacationLength = Integer.parseInt(newValue);

            }
        });

        // Set listeners for sliders
        foodSlider.valueProperty().addListener((observable, oldValue, newValue) -> updateTextFields());
        roomAndBoardSlider.valueProperty().addListener((observable, oldValue, newValue) -> updateTextFields());
        spendingSlider.valueProperty().addListener((observable, oldValue, newValue) -> updateTextFields());

        // Initialize text fields based on sliders
        updateTextFields();

        //sample code for loading attractions/hotels/reviews
        List<Activity> attractions = getAttractions();
        List<Activity> hotels = getAttractions();
        List<Activity> reviews = getAttractions();

        loadGrid(attractions, attractionsGrid);
        loadGrid(hotels, hotelsGrid);
        loadGrid(reviews, reviewsGrid);
    }

    private void setupTextFieldListeners() {
        // Listener for Room and Board Text Field
        roomAndBoardTextField.focusedProperty().addListener((observable, oldValue, isFocused) -> {
            if (!isFocused) {
                updateSliderFromTextField(roomAndBoardTextField, roomAndBoardSlider);
            }
        });

        // Listener for Food Text Field
        foodTextField.focusedProperty().addListener((observable, oldValue, isFocused) -> {
            if (!isFocused) {
                updateSliderFromTextField(foodTextField, foodSlider);
            }
        });

        // Listener for Spending Text Field
        spendingTextField.focusedProperty().addListener((observable, oldValue, isFocused) -> {
            if (!isFocused) {
                updateSliderFromTextField(spendingTextField, spendingSlider);
            }
        });
    }

    private void updateSliderFromTextField(TextField textField, Slider slider) {
        try {
            String text = textField.getText();
            if (text.startsWith("$")) {
                text = text.substring(1);  // Remove the dollar sign
            }
            double value = Double.parseDouble(text);
            if (value <= slider.getMax() && value >= slider.getMin()) {
                slider.setValue(value);
                adjustSlidersPostUpdate(slider);
            }
        } catch (NumberFormatException e) {
            // Handle invalid number format
            textField.setText(String.format("$%.2f", slider.getValue())); // Reset to slider's value
        }
    }

    private void adjustSlidersPostUpdate(Slider adjustedSlider) {
        // Recalculate the distribution of the remaining budget among the other sliders
        double totalUsed = roomAndBoardSlider.getValue() + foodSlider.getValue() + spendingSlider.getValue();
        if (totalUsed > dailyBudget) {
            double excess = totalUsed - dailyBudget;
            // Adjust other sliders proportionally to remove the excess
            for (Slider slider : new Slider[]{roomAndBoardSlider, foodSlider, spendingSlider}) {
                if (slider != adjustedSlider) {
                    double oldValue = slider.getValue();
                    slider.setValue(oldValue - excess / 2);  // Simple distribution of excess
                }
            }
        }
        updateTextFields();  // Update text fields to reflect the new slider values
    }

    private void loadGrid(List<Activity> activities, GridPane gridPane){
        int currGridColumn = 0;
        int currGridRow = 0;

        for(Activity activity : activities) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/activityCard.fxml"));

            try {
                HBox hbox = fxmlLoader.load();
                ActivityCardController activityCardController = fxmlLoader.getController();
                activityCardController.setActivity(activity);
                activityCardController.setNode(hbox);
                activityCardController.setParentController(this);
                gridPane.add(hbox, currGridColumn, currGridRow++);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadToAddedGrid(Activity activity){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/smallActivityCard.fxml"));

        try {
            HBox hbox = fxmlLoader.load();
            ActivityCardController activityCardController = fxmlLoader.getController();
            activityCardController.setActivity(activity);
            activityCardController.setNode(hbox);
            activityCardController.setParentController(this);
            addedGrid.add(hbox, currGridColumn, currGridRow++);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToSelectedActivities(Activity activity){
        loadToAddedGrid(activity);
        continueButton.setVisible(true);
    }

    public void handleContinueButton(){

    }

    private List<Activity> getAttractions(){
        Activity activity = new Activity();
        activity.setName("The Italian Restaurant");
        activity.setDescription("Rome, Italy");

        List<Activity> attractions = new ArrayList<>();
        attractions.add(activity);
        attractions.add(activity);
        attractions.add(activity);
        attractions.add(activity);
        attractions.add(activity);

        return attractions;
    }

    private void setupFieldListeners() {
        totalBudgetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                totalBudget = Double.parseDouble(newValue);
                updateDailyBudget();
            } catch (NumberFormatException e) {
                totalBudget = 0;
                updateDailyBudget();
            }
        });

        vacationLengthTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                vacationLength = Integer.parseInt(newValue);
                updateDailyBudget();
            } catch (NumberFormatException e) {
                vacationLength = 0;
                updateDailyBudget();
            }
        });
    }

    private void updateDailyBudget() {
        if (totalBudget > 0 && vacationLength > 0) {
            dailyBudget = totalBudget / vacationLength;
            updateSliders();
        } else {
            dailyBudget = 0;
            updateSliders();
        }
    }

    private void setupSliderListeners() {
        roomAndBoardSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            adjustOtherSliders(roomAndBoardSlider, newValue.doubleValue(), foodSlider, spendingSlider);
        });

        foodSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            adjustOtherSliders(foodSlider, newValue.doubleValue(), roomAndBoardSlider, spendingSlider);
        });

        spendingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            adjustOtherSliders(spendingSlider, newValue.doubleValue(), roomAndBoardSlider, foodSlider);
        });
    }

    private void adjustOtherSliders(Slider adjustedSlider, double newValue, Slider slider1, Slider slider2) {
        double totalUsed = newValue + slider1.getValue() + slider2.getValue();
        double remainingBudget = dailyBudget - newValue;

        if (totalUsed > dailyBudget) {
            double scale = remainingBudget / (slider1.getValue() + slider2.getValue());
            slider1.setValue(slider1.getValue() * scale);
            slider2.setValue(slider2.getValue() * scale);
        }

        updateTextFields();
    }

    private void updateSliders() {

        // Set the max value of the sliders to the daily budget
        roomAndBoardSlider.setMax(dailyBudget);
        foodSlider.setMax(dailyBudget);
        spendingSlider.setMax(dailyBudget);

        roomAndBoardSlider.setValue(dailyBudget * 0.50);
        foodSlider.setValue(dailyBudget * 0.30);
        spendingSlider.setValue(dailyBudget * 0.20);
        updateTextFields();
        System.out.println("Daily budget: " + dailyBudget + ", Room & Board: " + roomAndBoardSlider.getValue() + ", Food: " + foodSlider.getValue() + ", Spending: " + spendingSlider.getValue());
    }

    private void updateTextFields() {
        roomAndBoardTextField.setText(String.format("$%.2f", roomAndBoardSlider.getValue()));
        foodTextField.setText(String.format("$%.2f", foodSlider.getValue()));
        spendingTextField.setText(String.format("$%.2f", spendingSlider.getValue()));
    }
}
