package com.travel.virtualtravelassistant.Utility;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.travel.virtualtravelassistant.AlbumControllers.Album;
import com.travel.virtualtravelassistant.AlbumControllers.UserImage;
import com.travel.virtualtravelassistant.User.CurrentUser;
import javafx.scene.image.Image;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FirebaseStorageAction {
    private static final String PROJECT_ID = "travel-app-f268d";

    private static final String BUCKET_ID = "travel-app-f268d.appspot.com";

    private static GoogleCredentials googleCredentials;

    private static Path USER_IMAGE_DIRECTORY = null;

    private static final Path DEFAULT_PROFILE_PIC = Path.of("src/main/resources/com/travel/virtualtravelassistant/Images/profile-pic.png");

    private FirebaseStorageAction(){}

    public static void uploadProfilePicture(File imageSelected){
        uploadImage("profile-pic.png", imageSelected.getAbsolutePath());
    }


    public static Image getProfilePicture(){
        String imagePath = "profile-pic.png";

        Image profilePic = getImage(imagePath);

        if(profilePic == null){
            return new Image("file:" + DEFAULT_PROFILE_PIC);
        }

        return profilePic;
    }

    private static Image getImage(String imagePath){
        GoogleCredentials credentials = getCredentials();

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        String fullPath = CurrentUser.getInstance().getUserInfo().getUID() + "/" + imagePath;

        Blob blob = storage.get(BUCKET_ID, fullPath);

        if(blob != null) {
            InputStream inputStream = new ByteArrayInputStream(blob.getContent());
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

        if(CurrentUser.getInstance().getUserInfo() != null){
            return folderName;
        }

        if(bucket.get(folderName) == null){
            bucket.create(folderName, new byte[0]);
        }
        return folderName;
    }

    private static String createImageFolder(Album album){
        GoogleCredentials credentials = getCredentials();

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        if(album.getFirestoreId() == null){
            String id = UUID.randomUUID().toString();
            String folderName = CurrentUser.getInstance().getUserInfo().getUID() + "/" + id + "/";

            Bucket bucket = storage.get(BUCKET_ID);

            if(bucket.get(folderName) == null){
                bucket.create(folderName, new byte[0]);
           }
            album.setFirestoreId(id);
        }
        return album.getFirestoreId();
    }

    public static String uploadAlbumImage(Album album){
        String albumId = createImageFolder(album);
        String imageId = UUID.randomUUID().toString();

        System.out.println(albumId);
        System.out.println(imageId);
        String folder = albumId + "/" + imageId;

        return uploadImage(folder, album.getLocalPathToCover());
    }


    public static Image getAlbumImages(String albumId, String imageName){
        String imagePath = CurrentUser.getInstance().getUserInfo().getUID() + "/" + albumId + "/" + imageName;

        return getImage(imagePath);
    }

    public static Image getImageWithURL(UserImage userImage){
        return new Image(userImage.getImageURL());
    }


    private static GoogleCredentials getCredentials(){
        if(googleCredentials == null) {
            try {
                googleCredentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/adminSDK.json"));
            } catch (IOException e) {
                System.out.println("Could not set google credentials.");
                throw new RuntimeException(e);
            }
        }
        return googleCredentials;
    }

    private static String createHttpURL(String imagePath){
        GoogleCredentials credentials = getCredentials();

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        BlobId blobId = BlobId.of(BUCKET_ID, imagePath);

        if(blobId != null){
            return storage.signUrl(
                    BlobInfo.newBuilder(blobId).build(),
                    365000,
                    TimeUnit.DAYS
            ).toString();
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
