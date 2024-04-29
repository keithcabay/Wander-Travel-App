package com.travel.virtualtravelassistant.Utility;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.travel.virtualtravelassistant.AlbumControllers.Album;
import com.travel.virtualtravelassistant.AlbumControllers.UserImage;
import com.travel.virtualtravelassistant.User.CurrentUser;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FirebaseStorageAction {
    private static final String PROJECT_ID = "travel-app-f268d";

    private static final String BUCKET_ID = "travel-app-f268d.appspot.com";

    private static GoogleCredentials googleCredentials;

    private static final Path DEFAULT_PROFILE_PIC = Path.of("src/main/resources/com/travel/virtualtravelassistant/Images/profile-pic.png");

    private FirebaseStorageAction(){}


    /***
     * Caches the profile picture and calls to save it in Firebase Storage
     * @param imageSelected which is the image file the user chose to set as there profile picture
     */
    public static void uploadProfilePicture(File imageSelected){
        CurrentUser.getInstance().getUserInfo().setProfile_picture(new Image(imageSelected.toURI().toString()));
        uploadImage("profile-pic.png", imageSelected.getAbsolutePath());
    }


    /***
     * Gets the users profile picture either from cache or Firebase Storage
     * @return profile picture as an Image
     */
    public static Image getProfilePicture(){
        if(CurrentUser.getInstance().getUserInfo().getProfile_picture() != null){
            return CurrentUser.getInstance().getUserInfo().getProfile_picture();
        }

        String imagePath = "profile-pic.png";

        Image profilePic = getImage(imagePath);

        if(profilePic == null){
            return new Image("file:" + DEFAULT_PROFILE_PIC);
        }

        return profilePic;
    }

    public static Image getFriendProfilePicture(String UID){
        String imagePath = "profile-pic.png";

        Image profilePic = getFriendImage(imagePath, UID);

        if(profilePic == null){
            return new Image("file:" + DEFAULT_PROFILE_PIC);
        }

        return profilePic;
    }

    private static Image getFriendImage(String imagePath, String friendUID){
        GoogleCredentials credentials = getCredentials();

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();

        String fullPath = friendUID + "/" + imagePath;

        Blob blob = storage.get(BUCKET_ID, fullPath);

        if(blob != null) {
            InputStream inputStream = new ByteArrayInputStream(blob.getContent());
            return new Image(inputStream);
        }

        return null;
    }


    /***
     * Gets image from Firebase Storage
     * @param imagePath which is the remote path to the image within the user's Firebase Storage folder
     * @return the image received as an Image
     */
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


    /***
     * Uploads image to Firebase Storage
     * @param folderPath the remote folder path where the image should be saved within the user's Firebase Storage folder
     * @param filePath the local path to the image being uploaded
     * @return a URL to be used to make HTTP requests to the image
     */
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


    /***
     * Creates the user's personal folder within Firebase Storage
     * @return the name of the personal folder
     */
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


    /***
     * Upload a user image to an album within Firebase Storage
     * @param album which is the album the image is being added to
     * @param userImage which is the userImage being added
     * @return a URL to be used to make HTTP requests to the image
     */
    public static String uploadImageToAlbum(Album album, UserImage userImage){
        String albumId = createImageFolder(album);
        String imageId = userImage.getFirestoreId();

        if(imageId == null){
            imageId = UUID.randomUUID().toString();
            userImage.setFirestoreId(imageId);
        }

        System.out.println(albumId);
        System.out.println(imageId);
        String folder = albumId + "/" + imageId;

        return uploadImage(folder, userImage.getLocalPathToImage());
    }


    public static Image getAlbumImages(String albumId, String imageName){
        String imagePath = CurrentUser.getInstance().getUserInfo().getUID() + "/" + albumId + "/" + imageName;

        return getImage(imagePath);
    }

    /***
     * Load an image from Firebase Storage through HTTP request with the URL
     * @param userImage which is the object containing the image's URL
     * @return the loaded image
     */
    public static Image getImageWithURL(UserImage userImage){
        return new Image(userImage.getImageURL());
    }


    /***
     * Sets Google credentials if it hasn't been set
     * @return the instance of Google credentials
     */
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


    /***
     * Created the URL to the image within Firebase Storage to use for HTTP requests
     * @param imagePath which is the path to the image within Firebase Storage in the user's folder
     * @return the URL
     */
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
}
