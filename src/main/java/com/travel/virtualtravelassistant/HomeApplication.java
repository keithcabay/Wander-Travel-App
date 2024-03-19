/*
package com.travel.virtualtravelassistant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;

public class HomeApplication extends Application {

    private static Stage primaryStage; // It's better to use an instance field for the primary stage.
    public static Firestore fstore;
    public static FirebaseAuth fauth;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage; // Assign the provided stage to your static field.
        initializeFirebase();

        // Change to show the initial view you want, e.g., hello-view.
        showView(" homeView");
    }

    public static void showView(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeFirebase() {
        // Initialize Firebase only once.
        if (fstore == null || fauth == null) {
            fstore = FirebaseInitializer.firebase(); // Your Firebase initialization logic
            fauth = FirebaseAuth.getInstance();
            System.out.println("Firebase initialized.");
        }
    }

    // This method might be called after a successful login to switch to the home view.
    public static void showHomeView() throws IOException {
        showView("homeView"); // Reuse showView with "home-view" as parameter.
    }

    public static void main(String[] args) {
        launch(args);
    }
}


 */
