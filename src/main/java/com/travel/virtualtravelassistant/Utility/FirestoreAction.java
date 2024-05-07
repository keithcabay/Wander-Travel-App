package com.travel.virtualtravelassistant.Utility;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.travel.virtualtravelassistant.Activity;
import com.travel.virtualtravelassistant.AlbumControllers.Album;
import com.travel.virtualtravelassistant.Inbox.Notification;
import com.travel.virtualtravelassistant.MainApplication;
import com.travel.virtualtravelassistant.TripInfo.Trip;
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
        albumData.put("album-cover-id", album.getAlbumCover().getFirestoreId());

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
                userImage.setFirestoreId((String)document.get("album-cover-id"));

                newAlbum.setAlbumCover(userImage);
                newAlbum.addImage(userImage);
                albums.add(newAlbum);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve albums from firebase.");
            e.printStackTrace();
        }

        return albums;
    }

    public static void updateAlbum(Album album){
        DocumentReference docRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("albums").document(album.getFirestoreId());

        Map<String, Object> updated = new HashMap<>();
        updated.put("title", album.getTitle());
        updated.put("album-cover", album.getAlbumCover().getImageURL());
        updated.put("album-cover-id", album.getAlbumCover().getFirestoreId());

        docRef.update(updated);
    }

    public static void deleteAlbum(Album album){
        DocumentReference albumDoc = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("albums")
                .document(album.getFirestoreId());

        albumDoc.delete();

    }

    public static String storeImage(Album album, UserImage userImage){
        CollectionReference usersRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("albums")
                .document(album.getFirestoreId())
                .collection("images");

        WriteBatch batch = MainApplication.fstore.batch();

        DocumentReference imageDoc = usersRef.document(userImage.getFirestoreId());

        Map<String, Object> imageData = new HashMap<>();
        imageData.put("url", userImage.getImageURL());
        imageData.put("caption", userImage.getCaption());
        batch.set(imageDoc, imageData);

        batch.commit();

        return userImage.getFirestoreId();
    }

    /***
     * Retrieves images of given album from firebase storage and adds to list in album object
     * @param album which is the album you are getting the images from
     */
    public static void getImages(Album album){
        album.getImages().clear();
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

    public static void updateImage(Album album, UserImage userImage){
        DocumentReference imageDoc = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("albums")
                .document(album.getFirestoreId())
                .collection("images").document(userImage.getFirestoreId());

        HashMap<String, Object> update = new HashMap<>();
        update.put("caption", userImage.getCaption());
        imageDoc.update(update);

        updateAlbum(album);
    }

    public static void deleteImage(Album album, UserImage userImage){
        ApiFuture<WriteResult> future = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("albums").document(album.getFirestoreId())
                .collection("images").document(userImage.getFirestoreId()).delete();
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
                notiMap.put("trip-id", notification.getTripInfo());


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
                String tripInfo = (String)document.get("trip-id");
                if(tripInfo != null){
                    notification.setTripInfo(tripInfo);
                }

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
                String userId = (String)document.get("email") ;

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

    public static void addTrip(Trip trip){
        DocumentReference tripsRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("trips").document();

        Map<String, Object> tripMap = new HashMap<>();
        tripMap.put("cover-image-url", trip.getUrlToFirstImage());
        tripMap.put("title", trip.getTitle());
        tripMap.put("budget", trip.getBudget());
        tripMap.put("length", trip.getLength());
        Set<String> friends = trip.getFriends();
        List<String> friendsList = null;
        if(!friends.isEmpty()) {
            friendsList = new ArrayList<>(friends);
            tripMap.put("friends", friendsList);
        }

        tripsRef.set(tripMap);


        CollectionReference activitiesCollection = tripsRef.collection("days");

        List<Set<Activity>> activities = trip.getActivities();
        WriteBatch batch = MainApplication.fstore.batch();


        int count = 0;
        for(Set<Activity> activitySet : activities){
            DocumentReference daysDoc = activitiesCollection.document(String.valueOf(count));
            daysDoc.set(Map.of("day", String.valueOf(count)));
            Map<String, Object> activityMap = new HashMap<>();

            CollectionReference activityRef = daysDoc.collection("activities");

            for(Activity activity : activitySet){
                activityMap = new HashMap<>();
                DocumentReference doc = activityRef.document();
                activityMap.put("location-id", activity.getLocation_id());
                activityMap.put("name", activity.getName());
                activityMap.put("url-more-info", activity.getUrlMoreInfo());
                activityMap.put("description", activity.getDescription());
                activityMap.put("city", activity.getCity());
                activityMap.put("country", activity.getCountry());
                doc.set(activityMap);
            }
            count++;
        }
    }

    public static List<Trip> getTrips(){
        CollectionReference tripsRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("trips");

        ApiFuture<QuerySnapshot> future = tripsRef.get();

        List<Trip> trips = new ArrayList<>();

        try {
            QuerySnapshot querySnapshot = future.get();
            for (QueryDocumentSnapshot document : querySnapshot) {
                String tripId = document.getId();

                Trip newTrip = new Trip();
                newTrip.setTripId(tripId);
                newTrip.setBudget((String)document.get("budget"));
                newTrip.setLength((String)document.get("length"));
                newTrip.setTitle((String)document.get("title"));

                List<String> friendsOnTrip = (List<String>) document.get("friends");
                if(friendsOnTrip != null) {
                    Set<String> friends = new HashSet<>(friendsOnTrip);
                    newTrip.setFriends(friends);
                }

                String url = (String)document.get("cover-image-url");
                if(url != null) {
                    newTrip.setUrlToFirstImage(url);
                    newTrip.setImage(new Image(url));
                }
                trips.add(newTrip);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve albums from firebase.");
            e.printStackTrace();
        }

        return trips;
    }

    public static void getActivities(Trip trip){
        CollectionReference tripCollection = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("trips").document(trip.getTripId()).collection("days");

        tripCollection.document("0");


        ApiFuture<QuerySnapshot> future = tripCollection.get();

        List<Set<Activity>> activities = new ArrayList<>();
        try {
            QuerySnapshot querySnapshot = future.get();
            for (QueryDocumentSnapshot document : querySnapshot) {
                Set<Activity> day = new HashSet<>();
                CollectionReference activityRef = document.getReference().collection("activities");
                ApiFuture<QuerySnapshot> activityFuture = activityRef.get();
                QuerySnapshot activityQuery = activityFuture.get();
                for(QueryDocumentSnapshot activityDoc : activityQuery){
                    Activity activity = new Activity();
                    activity.setName((String)activityDoc.get("name"));
                    System.out.println(activity.getName());
                    activity.setCity((String) activityDoc.get("city"));
                    activity.setCountry((String) activityDoc.get("country"));
                    activity.setDescription((String)activityDoc.get("description"));
                    activity.setLocation_id((String)activityDoc.get("location-id"));
                    activity.setUrlMoreInfo((String)activityDoc.get("url-more-info"));
                    day.add(activity);
                }
                activities.add(day);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve activities from firebase.");
            e.printStackTrace();
        }
        trip.setActivities(activities);
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

    public static Trip getFriendTrip(Notification notification) {
        Trip trip = getFriendTripInfo(notification);
        getFriendTripActivities(trip, notification);
        return trip;
    }
    private static Trip getFriendTripInfo(Notification notification){
        DocumentReference tripsRef = MainApplication.fstore.collection("Users")
                .document(notification.getSenderEmail())
                .collection("trips").document(notification.getTripInfo());

        ApiFuture<DocumentSnapshot> future = tripsRef.get();
        Trip newTrip = new Trip();
        try {
            DocumentSnapshot document = future.get();
            String tripId = document.getId();
            newTrip.setTripId(tripId);
            newTrip.setBudget((String)document.get("budget"));
            newTrip.setLength((String)document.get("length"));
            newTrip.setTitle((String)document.get("title"));

            List<String> friendsOnTrip = (List<String>) document.get("friends");
            if(friendsOnTrip != null) {
                Set<String> friends = new HashSet<>(friendsOnTrip);
                friends.add(notification.getSenderEmail());
                newTrip.setFriends(friends);
            }

            String url = (String)document.get("cover-image-url");
            if(url != null) {
                newTrip.setUrlToFirstImage(url);
                newTrip.setImage(new Image(url));
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve albums from firebase.");
            e.printStackTrace();
        }

        return newTrip;}

    private static void getFriendTripActivities(Trip trip, Notification notification){
        CollectionReference tripCollection = MainApplication.fstore.collection("Users")
                .document(notification.getSenderEmail())
                .collection("trips").document(notification.getTripInfo()).collection("days");

        ApiFuture<QuerySnapshot> future = tripCollection.get();

        List<Set<Activity>> activities = new ArrayList<>();
        try {
            QuerySnapshot querySnapshot = future.get();
            for (QueryDocumentSnapshot document : querySnapshot) {
                Set<Activity> day = new HashSet<>();
                CollectionReference activityRef = document.getReference().collection("activities");
                ApiFuture<QuerySnapshot> activityFuture = activityRef.get();
                QuerySnapshot activityQuery = activityFuture.get();
                for(QueryDocumentSnapshot activityDoc : activityQuery){
                    Activity activity = new Activity();
                    activity.setName((String)activityDoc.get("name"));
                    System.out.println(activity.getName());
                    activity.setCity((String) activityDoc.get("city"));
                    activity.setCountry((String) activityDoc.get("country"));
                    activity.setDescription((String)activityDoc.get("description"));
                    activity.setLocation_id((String)activityDoc.get("location-id"));
                    activity.setUrlMoreInfo((String)activityDoc.get("url-more-info"));
                    day.add(activity);
                }
                activities.add(day);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve activities from firebase.");
            e.printStackTrace();
        }
        trip.setActivities(activities);
    }

    public static UserInfo getUserInfo(String email) {
        DocumentReference userRef = MainApplication.fstore.collection("Users")
                .document(email);

        ApiFuture<DocumentSnapshot> doc = userRef.get();
        UserInfo userInfo = new UserInfo();

        try {
            DocumentSnapshot document = doc.get();
            userInfo.setUID(email);
            userInfo.setFirst_name((String) document.get("first_name"));
            userInfo.setLast_name((String) document.get("last_name"));

            Image image = FirebaseStorageAction.getFriendProfilePicture(email);
            userInfo.setProfile_picture(image);
        } catch (Exception e) {
        }
        return userInfo;
    }

    public static void addFriendToTrip(String tripId, String senderUID, String receiverUID){
        DocumentReference tripsRef = MainApplication.fstore.collection("Users")
                .document(senderUID)
                .collection("trips").document(tripId);

        ApiFuture<DocumentSnapshot> doc = tripsRef.get();

        try {
            DocumentSnapshot docRef = doc.get();
            List<String> friends = (List<String>) docRef.get("friends");
            if(friends == null) {
                friends = new ArrayList<>();
                friends.add(receiverUID);
            }
            docRef.getReference().update("friends", friends);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
