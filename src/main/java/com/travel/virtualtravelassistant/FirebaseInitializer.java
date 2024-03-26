package com.travel.virtualtravelassistant;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseInitializer {
    public static Firestore firebase() {
        try {
            System.out.println("Initializing Firebase...");
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/adminSDK.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("travel-app-f268d.appspot.com")
                    .build();
            // notifies if Firebase was initialized
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully.");
            } else {
                System.out.println("Firebase already initialized.");
            }
            // catches in case Firebase cannot initialize
        } catch (IOException ex) {
            System.err.println("Failed to initialize Firebase");
            ex.printStackTrace();
            System.exit(1);
        }
        return FirestoreClient.getFirestore();
    }
}
