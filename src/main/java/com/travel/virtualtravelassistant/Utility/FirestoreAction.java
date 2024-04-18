package com.travel.virtualtravelassistant.Utility;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.travel.virtualtravelassistant.AlbumControllers.Album;
import com.travel.virtualtravelassistant.MainApplication;
import com.travel.virtualtravelassistant.User.CurrentUser;
import com.travel.virtualtravelassistant.AlbumControllers.UserImage;

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
        imageData.put("caption", null);
        batch.set(imageDoc, imageData);

        userImage.setFirestoreId(imageDocId);
        batch.commit();

        return imageDocId;
    }
}
