package com.travel.virtualtravelassistant;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class HomeController {

    @FXML
    private Label destinationLabel;
    @FXML
    private ImageView destinationImageView1;
    @FXML
    private ImageView destinationImageView2;
    @FXML
    private ImageView destinationImageView3;
    @FXML
    private ImageView destinationImageView4;

    private defaultImages defaultImages;
    private List<String> imageUrls;
    private int currentIndex = 0;

    @FXML
    private void planTrip() {
        // Implement your logic for planning a trip
        // empty for now
        System.out.println("Planning a new trip...");
    }

    @FXML
    private void exploreSuggestions() {
        // Implement your logic for exploring suggestions
        // empty for now
        System.out.println("Exploring suggestions...");
    }

    @FXML
    protected void handleCloseClick() {
        Platform.exit();
        System.out.println("Exit Clicked");
    }

    @FXML
    private void initialize() {
        defaultImages = new defaultImages();
        fetchDestinationImages();
    }

    public void fetchDestinationImages() {
        imageUrls = defaultImages.fetchImageUrls();
        if (!imageUrls.isEmpty()) {
            updateImageViews();
        }
    }

    private void updateImageViews() {
        for (int i = 0; i < Math.min(imageUrls.size(), 4); i++) {
            String imageUrl = imageUrls.get((currentIndex + i) % imageUrls.size());
            Image image = new Image(imageUrl);
            switch (i) {
                case 0:
                    destinationImageView1.setImage(image);
                    break;
                case 1:
                    destinationImageView2.setImage(image);
                    break;
                case 2:
                    destinationImageView3.setImage(image);
                    break;
                case 3:
                    destinationImageView4.setImage(image);
                    break;
            }
        }
        currentIndex = (currentIndex + 4) % imageUrls.size();
    }
}
