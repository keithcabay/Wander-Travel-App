package com.travel.virtualtravelassistant.Utility;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.travel.virtualtravelassistant.AlbumControllers.Album;
import com.travel.virtualtravelassistant.Inbox.Notification;
import com.travel.virtualtravelassistant.MainApplication;
import com.travel.virtualtravelassistant.User.CurrentUser;
import com.travel.virtualtravelassistant.AlbumControllers.UserImage;
import com.travel.virtualtravelassistant.User.UserInfo;
import javafx.scene.image.Image;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class FirestoreAction {

    private FirestoreAction(){};

    public static void storeAlbumInfo(Album album){
        CollectionReference usersRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("albums");


        DocumentReference albumDoc = usersRef.document(album.getFirestoreId());

        Map<String, Object> albumData = new HashMap<>();
        albumData.put("title", album.getTitle());
        albumData.put("album-cover", album.getAlbumCover().getImageURL());

        WriteBatch batch = MainApplication.fstore.batch();
        batch.set(albumDoc, albumData);


        try {
            batch.commit();
            storeImage(album, album.getAlbumCover());
        }catch (Exception e){
            System.out.println("Could not add new register to DB");
        }
    }

    public static List<Album> getAlbums(){
        List<Album> albums = new ArrayList<>();

        CollectionReference albumsRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("albums");

        ApiFuture<QuerySnapshot> future = albumsRef.get();


        try {
            QuerySnapshot querySnapshot = future.get();
            for (QueryDocumentSnapshot document : querySnapshot) {
                String albumId = document.getId();

                Album newAlbum = new Album();
                newAlbum.setFirestoreId(albumId);
                newAlbum.setTitle((String)document.get("title"));

                UserImage userImage = new UserImage();
                userImage.setImageURL((String)document.get("album-cover"));

                newAlbum.setAlbumCover(userImage);
                albums.add(newAlbum);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve albums from firebase.");
            e.printStackTrace();
        }

        return albums;
    }

    public static String storeImage(Album album, UserImage userImage){
        CollectionReference usersRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("albums")
                .document(album.getFirestoreId())
                .collection("images");

        WriteBatch batch = MainApplication.fstore.batch();

        String imageDocId = UUID.randomUUID().toString();

        DocumentReference imageDoc = usersRef.document(imageDocId);

        Map<String, Object> imageData = new HashMap<>();
        imageData.put("url", userImage.getImageURL());
        imageData.put("caption", userImage.getCaption());
        batch.set(imageDoc, imageData);

        userImage.setFirestoreId(imageDocId);
        batch.commit();

        return imageDocId;
    }

    /***
     * Retrieves images of given album from firebase storage and adds to list in album object
     * @param album which is the album you are getting the images from
     */
    public static void getImages(Album album){
        CollectionReference imagesRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("albums")
                .document(album.getFirestoreId())
                .collection("images");

        ApiFuture<QuerySnapshot> future = imagesRef.get();

        try {
            QuerySnapshot querySnapshot = future.get();
            for (QueryDocumentSnapshot document : querySnapshot) {
                String imageId = document.getId();

                UserImage userImage = new UserImage();
                userImage.setFirestoreId(imageId);
                userImage.setImageURL((String)document.get("url"));
                userImage.setCaption((String)document.get("caption"));


                album.addImage(userImage);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve images from firebase.");
            e.printStackTrace();
        }

    }


    /***
     * Save profile changes in Firestore
     */
    public static void saveProfileChanges(String firstName, String lastName, String location, String bio) {
        DocumentReference usersRef = MainApplication.fstore.collection("Users").document(CurrentUser.getInstance().getUserInfo().getUID());

        Map<String, Object> userData = new HashMap<>();
        userData.put("first_name", firstName);
        userData.put("last_name", lastName);
        userData.put("location", location);
        userData.put("bio", bio);


        try {
            usersRef.update(userData);
        }catch (Exception e){
            System.out.println("Could not add new register to DB");
        }
    }


    /***
     * Send a message/notification and store in receivers collection
     * Must set receiver email, sender email, and notification type
     * @param notification the object storing info to store notification
     */
    public static void sendNotification(Notification notification) {
        DocumentReference docRef = MainApplication.fstore.collection("Users")
                .document(notification.getReceiverEmail());

        ApiFuture<DocumentSnapshot> future = docRef.get();

        try{
            DocumentSnapshot doc = future.get();
            if(!doc.exists()){
                System.out.println("Receiving user does not exist.");
                return;
            }else{
                DocumentReference notiRef = docRef.collection("notifications").document();
                Map<String, Object> notiMap = new HashMap<>();
                notiMap.put("receiver-email", notification.getReceiverEmail());
                notiMap.put("sender-email", notification.getSenderEmail());
                notiMap.put("sender-first-name", notification.getSenderFirstName());
                notiMap.put("sender-last-name", notification.getSenderLastName());
                notiMap.put("notification-type", notification.getNotification_type());
                notiMap.put("message", notification.getMessage());


                notiRef.set(notiMap);
            }
        }catch (Exception e){
            System.out.println("Failed to retrieve document in Firestore.");
        }
    }

    public static List<Notification> getNotifications(){
        List<Notification> notifications = new ArrayList<>();

        CollectionReference notiRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("notifications");

        ApiFuture<QuerySnapshot> future = notiRef.get();


        try {
            QuerySnapshot querySnapshot = future.get();
            for (QueryDocumentSnapshot document : querySnapshot) {
                String notification_id = document.getId();

                Notification notification = new Notification();
                notification.setNotification_id(notification_id);
                notification.setNotification_type((String)document.get("notification-type"));
                notification.setSenderEmail((String)document.get("sender-email"));
                notification.setSenderFirstName((String)document.get("sender-first-name"));
                notification.setSenderLastName((String)document.get("sender-last-name"));
                notification.setMessage((String)document.get("message"));
                notification.setReceiverEmail(CurrentUser.getInstance().getUserInfo().getUID());

                notifications.add(notification);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve albums from firebase.");
            e.printStackTrace();
        }
        return notifications;
    }

    public static void deleteNotification(Notification notification){
        DocumentReference notiRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("notifications")
                .document(notification.getNotification_id());

        notiRef.delete();
    }

    public static void addFriend(Notification notification){
        DocumentReference friendsRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("friends").document();

        Map<String, Object> friendMap = new HashMap<>();
        friendMap.put("email", notification.getSenderEmail());
        friendMap.put("first-name", notification.getSenderFirstName());
        friendMap.put("last-name", notification.getSenderLastName());

        friendsRef.set(friendMap);

        friendsRef = MainApplication.fstore.collection("Users")
                .document(notification.getSenderEmail().toLowerCase())
                .collection("friends").document();

        friendMap = new HashMap<>();
        friendMap.put("email", CurrentUser.getInstance().getUserInfo().getUID());
        friendMap.put("first-name", CurrentUser.getInstance().getUserInfo().getFirst_name());
        friendMap.put("last-name", CurrentUser.getInstance().getUserInfo().getLast_name());
        friendsRef.set(friendMap);
    }



    public static Set<UserInfo> getFriends(){
        Set<UserInfo> friends = new HashSet<>();

        CollectionReference notiRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("friends");

        ApiFuture<QuerySnapshot> future = notiRef.get();


        try {
            QuerySnapshot querySnapshot = future.get();
            for (QueryDocumentSnapshot document : querySnapshot) {
                String userId = (String)document.get("email");

                UserInfo userInfo = new UserInfo();
                userInfo.setUID(userId);
                userInfo.setFirst_name((String)document.get("first-name"));
                userInfo.setLast_name((String)document.get("last-name"));

                Image image = FirebaseStorageAction.getFriendProfilePicture(userId);
                userInfo.setProfile_picture(image);

                friends.add(userInfo);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve albums from firebase.");
            e.printStackTrace();
        }
        return friends;

    }
    public static boolean doesUserExist(String email){
        DocumentReference docRef = MainApplication.fstore.collection("Users")
                .document(email);

        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            DocumentSnapshot doc = future.get();
            if (!doc.exists()) {
                System.out.println("Receiving user does not exist.");
                return false;
            }
        }catch (Exception e) {
            System.out.println("Could not load document from firestore");
        }
        return true;
    }
}
