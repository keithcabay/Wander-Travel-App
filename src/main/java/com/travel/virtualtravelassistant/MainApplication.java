package com.travel.virtualtravelassistant;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import javafx.stage.Window;

public class MainApplication extends Application {

    private static Stage primaryStage; // Use an instance field for the primary stage.
    public static Firestore fstore;
    public static FirebaseAuth fauth;
    public static Bucket firebaseStorage;

    private static Dialog chatBot;
    private static boolean chatbotOpen;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        initializeFirebase(); //initialize firebase once application runs
        showLoginView(); // Show the login view on application start.
    }

    private static void showChatbotPopup() throws IOException {
        if (primaryStage == null) {
            System.out.println("Primary Stage is null");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("ChatbotUI.fxml"));
        Parent root = fxmlLoader.load();

        Dialog dialog = new Dialog();
        dialog.initOwner(primaryStage);
        dialog.getDialogPane().setContent(root);
        dialog.setTitle("Chatbot");
        dialog.setResizable(true);
        dialog.initModality(Modality.NONE); // Dialog does not block other user interaction
        chatBot = dialog;
    }

    public static void showOrHideChatbot(){
        if(chatBot == null){
            try {
                showChatbotPopup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(chatbotOpen){
            Window window = chatBot.getDialogPane().getScene().getWindow();
            window.hide();
            chatbotOpen = false;
        }else{
            chatbotOpen = true;
            chatBot.show();
        }
    }

    public static boolean getIsChatbotOpen(){
        return chatbotOpen;
    }

    public static void showLoginView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("LogIn.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void showHomeView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("homeView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Home");
    }

    public static void showProfileView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("profilePage.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Profile Page");
    }

    public static void showPlanNextTripView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("PlanNextTrip.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Plan Next Trip");
    }

    private void initializeFirebase() {
        // Initialize Firebase only once.
        if (fstore == null || fauth == null || firebaseStorage == null) {
            fstore = FirebaseInitializer.firebase(); // Assuming FirebaseInitializer correctly initializes Firestore
            fauth = FirebaseAuth.getInstance();
            firebaseStorage = StorageClient.getInstance().bucket();
            System.out.println("Firebase initialized.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
