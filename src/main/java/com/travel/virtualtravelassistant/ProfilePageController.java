package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.AlbumControllers.Album;
import com.travel.virtualtravelassistant.TripInfo.Trip;
import com.travel.virtualtravelassistant.TripInfo.TripPreviewCardController;
import com.travel.virtualtravelassistant.User.CurrentUser;
import com.travel.virtualtravelassistant.Utility.ApplicationUtil;
import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfilePageController {

    @FXML
    ImageView imageView;

    @FXML
    GridPane upcomingTripsGrid;

    @FXML
    Label locationLabel;

    @FXML
    Label bio;

    @FXML
    Label userName;

    public void initialize(){
        Image profile_pic = FirebaseStorageAction.getProfilePicture();
        ApplicationUtil.setImageViewRoundCroppedProfilePic(imageView, profile_pic);

        userName.setText(CurrentUser.getInstance().getUserInfo().getFirst_name() + " " +  CurrentUser.getInstance().getUserInfo().getLast_name());
        if(CurrentUser.getInstance().getUserInfo().getBio() != null){
            bio.setText(CurrentUser.getInstance().getUserInfo().getBio());
        }
        if(CurrentUser.getInstance().getUserInfo().getLocation() != null){
            locationLabel.setText(CurrentUser.getInstance().getUserInfo().getLocation());
        }

        onUpcomingTripsClick();
    }

    public void onUpcomingTripsClick(){
        List<Trip> trips = getTrips();
        int column = 0;
        int row = 0;

        for(Trip trip : trips){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("tripPreviewCard.fxml"));
            try {
                Pane pane = fxmlLoader.load();
                TripPreviewCardController tripPreviewCardController = fxmlLoader.getController();
                tripPreviewCardController.setUpcomingTrip(trip);
                if(column == 2){
                    column = 0;
                    row++;
                }

                upcomingTripsGrid.add(pane, column++, row);
                upcomingTripsGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                upcomingTripsGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onPreviousTripsClick(){
        upcomingTripsGrid.getChildren().clear();
        List<Trip> trips = getPreviousTrips();
        int column = 0;
        int row = 0;

        for(Trip trip : trips){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("tripPreviewCard.fxml"));
            try {
                Pane pane = fxmlLoader.load();
                TripPreviewCardController tripPreviewCardController = fxmlLoader.getController();
                tripPreviewCardController.setPreviousTrip(trip);
                if(column == 2){
                    column = 0;
                    row++;
                }

                upcomingTripsGrid.add(pane, column++, row);
                upcomingTripsGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                upcomingTripsGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onEditProfileClick(ActionEvent event){
        try {
            Parent parent = FXMLLoader.load((getClass().getResource("profileSettings.fxml")));
            Scene scene = new Scene(parent);

            // Get the Stage from the ActionEvent
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load " + "profileSettings.fxml" +  " page.");
        }
    }

    private List<Trip> getTrips(){
        List<Trip> list = FirestoreAction.getTrips();

        return list;
    }

    private List<Trip> getPreviousTrips(){
        List<Album> albums = FirestoreAction.getAlbums();
        List<Trip> trips = new ArrayList<>();

        for(Album album : albums){
            Image image = new Image(album.getAlbumCover().getImageURL());
            Trip trip = new Trip();
            trip.setImage(image);
            trip.setTitle(album.getTitle());
            trips.add(trip);
        }
        return trips;
    }

}
