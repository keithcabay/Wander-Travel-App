package com.travel.virtualtravelassistant.Utility;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.travel.virtualtravelassistant.Album;
import com.travel.virtualtravelassistant.MainApplication;
import com.travel.virtualtravelassistant.User.CurrentUser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirestoreAction {

    private FirestoreAction(){};

    public static String storeAlbum(Album album){
        CollectionReference usersRef = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("albums");

        DocumentReference albumDoc = usersRef.document();

        Map<String, Object> albumData = new HashMap<>();
        albumData.put("title", album.getTitle());
        albumData.put("album-cover", album.getAlbumCoverURL());

        WriteBatch batch = MainApplication.fstore.batch();
        batch.set(albumDoc, albumData);

        CollectionReference imagesRef = albumDoc.collection("images");

        DocumentReference imageDoc = usersRef.document();

        Map<String, Object> imageData = new HashMap<>();
        imageData.put("url", album.getAlbumCover());
        imageData.put("caption", null);
        batch.set(imageDoc, imageData);

        try {
            batch.commit();
        }catch (Exception e){
            System.out.println("Could not add new register to DB");
        }

        return albumDoc.getId();
    }

    public static Map<String, String> getAlbums(){
        Map<String, String> albumIdToTitle = new HashMap<>();

        ApiFuture<QuerySnapshot> future = MainApplication.fstore.collection("Users")
                .document(CurrentUser.getInstance().getUserInfo().getUID())
                .collection("albums")
                .get();

        try {
            QuerySnapshot querySnapshot = future.get();
            for (QueryDocumentSnapshot document : querySnapshot) {
                //(albumId -> album title)
                albumIdToTitle.put(document.getId(), (String)document.getData().get("title"));
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to retrieve albums from firebase.");
            e.printStackTrace();
        }

        return albumIdToTitle;
    }
}
