package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.TripInfo.Trip;
import com.travel.virtualtravelassistant.TripInfo.TripPreviewCardController;
import com.travel.virtualtravelassistant.User.CurrentUser;
import com.travel.virtualtravelassistant.Utility.FirebaseStorageAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    Label userName;

    public void initialize(){
        userName.setText(CurrentUser.getInstance().getUserInfo().getFirst_name() + " " +  CurrentUser.getInstance().getUserInfo().getLast_name());
        imageView.setImage(FirebaseStorageAction.getProfilePicture());

        List<Trip> trips = getTrips();
        int column = 0;
        int row = 0;

        for(Trip trip : trips){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("tripPreviewCard.fxml"));
            try {
                Pane pane = fxmlLoader.load();
                TripPreviewCardController tripPreviewCardController = fxmlLoader.getController();
                tripPreviewCardController.setTrip(trip);
                if(column == 2){
                    column = 0;
                    row++;
                }

                upcomingTripsGrid.add(pane, column++, row);
                upcomingTripsGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                upcomingTripsGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                upcomingTripsGrid.setMaxHeight(Region.USE_PREF_SIZE);

                upcomingTripsGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                upcomingTripsGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                upcomingTripsGrid.setMaxHeight(Region.USE_PREF_SIZE);
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
        List<Trip> list = new ArrayList<>();

        Trip trip1 = new Trip();
        trip1.setCityName("Clear Water Beaches");
        trip1.setCountryName("United States");
        trip1.setBudget(2000);
        trip1.setLength(7);
        trip1.setImageSource("/com/travel/virtualtravelassistant/Images/clear-water-beaches-florida-scaled.jpg");
        list.add(trip1);
        list.add(trip1);
        list.add(trip1);
        list.add(trip1);
        list.add(trip1);
        list.add(trip1);
        list.add(trip1);
        list.add(trip1);
        list.add(trip1);
        list.add(trip1);


        return list;
    }
}
