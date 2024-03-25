package com.travel.virtualtravelassistant.Utility;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.travel.virtualtravelassistant.User.CurrentUser;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FirebaseStorageAction {
    private static final String PROJECT_ID = "travel-app-f268d";

    private static final String BUCKET_ID = "travel-app-f268d.appspot.com";

    private static Path USER_IMAGE_DIRECTORY = null;

    private static final Path DEFAULT_PROFILE_PIC = Path.of("src/main/resources/com/travel/virtualtravelassistant/Images/profile-pic.png");


    public static void uploadProfilePicture(File imageSelected){
        if(USER_IMAGE_DIRECTORY != null) {
            try {
                Files.copy(Path.of(imageSelected.toURI()), USER_IMAGE_DIRECTORY.resolve("profile-pic.png"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Could not replace existing profile locally.");
                throw new RuntimeException(e);
            }
        }

        uploadImage(CurrentUser.getInstance().getUserInfo().getUID(), "profile-pic.png", imageSelected.getAbsolutePath());
    }

    public static Image getProfilePicture(){
        if(USER_IMAGE_DIRECTORY != null){
            if(Files.exists(USER_IMAGE_DIRECTORY.resolve("profile-pic.png"))){
                return new Image("file:" + USER_IMAGE_DIRECTORY.toString() + "/profile-pic.png");
            }
        }else {
            String imagePath = CurrentUser.getInstance().getUserInfo().getUID() + "/" + "profile-pic.png";
            if(getImage(imagePath)) {
                return new Image("file:" + USER_IMAGE_DIRECTORY.toString() + "/profile-pic.png");
            }
        }

        System.out.println("Getting called");
        return new Image("file:" + DEFAULT_PROFILE_PIC);
    }

    private static boolean getImage(String imagePath){
        GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/adminSDK.json"));
        } catch (IOException e) {
            System.out.println("Could not set google credentials.");
            throw new RuntimeException(e);
        }
        System.out.println("Attempting to get service.");
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        Blob blob = storage.get(BUCKET_ID, imagePath);

        if(blob != null){
            System.out.println("Attempting to Create directory.");
            createTempDirectory();
            System.out.println("Attempting to download profile-pic.png");
            blob.downloadTo(USER_IMAGE_DIRECTORY.resolve("profile-pic.png"));
            System.out.println("Downloaded.");
            return true;
        }else{
            return false;
        }
    }


    private static void uploadImage(String UID, String imageName, String filePath){
        GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/adminSDK.json"));
        } catch (IOException e) {
            System.out.println("Could not set google credentials.");
            throw new RuntimeException(e);
        }
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        String folderName = createUserFolder(UID);

        BlobId blobId = BlobId.of(BUCKET_ID, folderName + imageName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        try {
            storage.createFrom(blobInfo, Paths.get(filePath));
            System.out.println("Imaged Saved.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to upload image.");
        }
    }

    private static String createUserFolder(String userId){

        GoogleCredentials credentials = null;
        try {
            credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/adminSDK.json"));
        } catch (IOException e) {
            System.out.println("Failed to set Google credentials.");
            e.printStackTrace();
            return null;
        }
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        String folderName = userId + "/";

        Bucket bucket = storage.get("travel-app-f268d.appspot.com");

        if(bucket.get(folderName) == null){
            bucket.create(folderName, new byte[0]);
        }
        return folderName;
    }

    private static void createTempDirectory(){
        if(USER_IMAGE_DIRECTORY == null){
            try {
                System.out.println("Creating temp directory");
                USER_IMAGE_DIRECTORY = Files.createTempDirectory("");
                System.out.println("Temp directory created");
            } catch (IOException e) {
                System.out.println("Could not create new directory");
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("Directory already exists.");
        }
    }

    public static void removeTempDirectory(){
        USER_IMAGE_DIRECTORY = null;
    }

}
