package com.travel.virtualtravelassistant;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;

public class MainApplication extends Application {

    private static Stage primaryStage; // Use an instance field for the primary stage.

    //firebase instance for Authentication and Database
    public static Firestore fstore;
    public static FirebaseAuth fauth;
    public static Bucket firebaseStorage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        //when application is closed, directory is reset
        primaryStage.setOnCloseRequest(e -> {
            FirebaseStorageAction.removeTempDirectory();
        });
        initializeFirebase(); //initialize firebase once application runs
        showLoginView(); // Show the login view on application start.
    }
    // Load and display the login view first
    public static void showLoginView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("LogIn.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    // opens home page after login
    public static void showHomeView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("homeView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Home");
    }
    // shows profile page if selected from NavBarController
    public static void showProfileView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("profilePage.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Profile Page");
    }
    // initializes firebase with null and authenticates
    private void initializeFirebase() {
        // Initialize Firebase only once.
        if (fstore == null || fauth == null || firebaseStorage == null) {
            fstore = FirebaseInitializer.firebase(); // Assuming FirebaseInitializer correctly initializes Firestore
            fauth = FirebaseAuth.getInstance();
            firebaseStorage = StorageClient.getInstance().bucket();
            System.out.println("Firebase initialized.");
        }
    }
    // main
    public static void main(String[] args) {
        launch(args);
    }
}
