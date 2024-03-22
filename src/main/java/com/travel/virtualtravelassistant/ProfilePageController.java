package com.travel.virtualtravelassistant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfilePageController {
    private List<Trip> trips;

    @FXML
    private GridPane upcomingTripsGrid;

    public void initialize(){
        trips = getTrips();
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
