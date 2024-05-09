package com.travel.virtualtravelassistant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
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
    @FXML
    private WebView webView;
    @FXML
    private Button playVideoButton;

    private defaultImages defaultImages; // Assuming this is a class you've defined
    private List<String> imageUrls;
    private int currentIndex = 0;

    @FXML
    private void initialize() {
        defaultImages = new defaultImages();
        fetchDestinationImages();

        configureVideo();
        configureButton();
    }

    private void configureVideo() {
        // webView already set up via FXML, just set the initial visibility
        //  webView.setVisible(false);
        // Loading the video URL in advance; if this is not desired, move to the button's action event
        // webView.getEngine().load("https://www.youtube.com/embed/JXMWOmuR1hU");

        System.out.println("Hello");
    }

    private void configureButton() {
        //   playVideoButton.setOnAction(e -> webView.setVisible(true));  // Toggle WebView visibility
        System.out.println("World");

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

    @FXML
    private void toggleVideoVisibility() {
        webView.setVisible(!webView.isVisible()); // This will toggle the visibility of the WebView
        if (webView.isVisible()) {
            webView.getEngine().reload(); // Reload the video when made visible again, optional
        }
    }
}
