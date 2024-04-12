package com.travel.virtualtravelassistant.Utility;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.travel.virtualtravelassistant.User.CurrentUser;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FirebaseStorageAction {
    private static final String PROJECT_ID = "travel-app-f268d";

    private static final String BUCKET_ID = "travel-app-f268d.appspot.com";

    private static GoogleCredentials googleCredentials;

    private static Path USER_IMAGE_DIRECTORY = null;

    private static final Path DEFAULT_PROFILE_PIC = Path.of("src/main/resources/com/travel/virtualtravelassistant/Images/profile-pic.png");

    private FirebaseStorageAction(){}

    public static void uploadProfilePicture(File imageSelected){
        if(USER_IMAGE_DIRECTORY != null) {
            try {
                Files.copy(Path.of(imageSelected.toURI()), USER_IMAGE_DIRECTORY.resolve("profile-pic.png"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Could not replace existing profile locally.");
                throw new RuntimeException(e);
            }
        }

        uploadImage("profile-pic.png", imageSelected.getAbsolutePath());
    }

    public static Image getProfilePicture(){
        String imagePath = CurrentUser.getInstance().getUserInfo().getUID() + "/" + "profile-pic.png";

        Image profilePic = getImage(imagePath);

        if(profilePic == null){
            return new Image("file:" + DEFAULT_PROFILE_PIC);
        }

        return profilePic;
    }

    private static Image getImage(String imagePath){
        GoogleCredentials credentials = getCredentials();

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        Blob blob = storage.get(BUCKET_ID, imagePath);
        System.out.println("HERE -1");

        if(blob != null) {
            System.out.println("HERE 0");
            InputStream inputStream = new ByteArrayInputStream(blob.getContent());
            System.out.println("HERE 1");
            return new Image(inputStream);
        }
        return null;
    }


    private static String uploadImage(String folderPath, String filePath){
        GoogleCredentials credentials = getCredentials();

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        String userBucket = createUserFolder();

        BlobId blobId = BlobId.of(BUCKET_ID, userBucket + folderPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        try {
            storage.createFrom(blobInfo, Paths.get(filePath));
            System.out.println("Imaged Saved.");
            return createHttpURL(userBucket + folderPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to upload image.");
        }

        return null;
    }

    public static String createUserFolder(){
        GoogleCredentials credentials = getCredentials();

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        String folderName = CurrentUser.getInstance().getUserInfo().getUID() + "/";

        Bucket bucket = storage.get(BUCKET_ID);

        if(bucket.get(folderName) == null){
            bucket.create(folderName, new byte[0]);
        }
        return folderName;
    }

    public static String createImageFolder(String imageFolderName){
        GoogleCredentials credentials = getCredentials();

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        String folderName = CurrentUser.getInstance().getUserInfo().getUID() + "/" + imageFolderName + "/";

        Bucket bucket = storage.get(BUCKET_ID);

        if(bucket.get(folderName) == null){
            bucket.create(folderName, new byte[0]);
        }
        return folderName;
    }

    public static String uploadAlbumImage(String folderName, String imageName, String filePath){
        System.out.println(folderName);
        System.out.println(imageName);
        String folder = folderName + "/" + imageName;

        return uploadImage(folder, filePath);
    }


    public static Image getAlbumImages(String albumId, String imageName){
        String imagePath = CurrentUser.getInstance().getUserInfo().getUID() + "/" + albumId + "/" + imageName;

        return getImage(imagePath);
    }


    private static GoogleCredentials getCredentials(){
        if(googleCredentials == null) {
            try {
                GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/adminSDK.json"));
                return credentials;
            } catch (IOException e) {
                System.out.println("Could not set google credentials.");
                throw new RuntimeException(e);
            }
        }else{
            return googleCredentials;
        }
    }

    private static String createHttpURL(String imagePath){
        GoogleCredentials credentials = getCredentials();

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        Blob blob = storage.get(BUCKET_ID, imagePath);

        if(blob != null){
            return blob.getMediaLink();
        }

        return null;
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
