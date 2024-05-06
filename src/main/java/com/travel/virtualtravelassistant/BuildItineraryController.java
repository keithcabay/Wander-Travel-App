package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.PlanNextTrip.PlanNextTripController;
import com.travel.virtualtravelassistant.TripInfo.Trip;
import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class BuildItineraryController {
    @FXML
    GridPane selectedGrid;

    @FXML
    GridPane addedGrid;

    @FXML
    Button saveItineraryButton;

    @FXML
    TextField nameField;
    @FXML
    TextField budgetField;
    private Trip trip;

    private int selectedDate = -1;
    private int selectedRow = -1;
    private DayCardController savedDayCard;
    private int totalRows = -1;

    private List<Set<Activity>> dailyActivities;
    private Map<String, List<Activity>> activities;
    private int currGridColumn = 0;
    private int currGridRow = 0;
    private int budget;
    private int length;

    public void initialize(){
        trip = new Trip();
        dailyActivities = new ArrayList<>();
        trip.setActivities(dailyActivities);
    }

    public void setActivities(Map<String, List<Activity>> activities){
        this.activities = activities;
        for(Map.Entry<String, List<Activity>> activityEntry : activities.entrySet()) {
            Label label = new Label(activityEntry.getKey());
            selectedGrid.add(label, currGridColumn, currGridRow++);
            List<Activity> activityList = activityEntry.getValue();
            for(Activity activity : activityList) {
                loadToSelectedGrid(activity);
            }
        }
    }

    public void setBudget(double budget){
        budgetField.setText(String.format("%.2f", budget));
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void setLength(int length) {
        this.length = length;
    }

    private void loadToSelectedGrid(Activity activity){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/smallActivityCard.fxml"));

        try {
            HBox hbox = fxmlLoader.load();
            ActivityCardController activityCardController = fxmlLoader.getController();
            activityCardController.setActivity(activity);
            activityCardController.setNode(hbox);
            activityCardController.setBuildItineraryParentController(this);
            selectedGrid.add(hbox, currGridColumn, currGridRow++);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadToAddedGrid(Activity activity){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/activityCard.fxml"));

        try {
            HBox hbox = fxmlLoader.load();
            ActivityCardController activityCardController = fxmlLoader.getController();
            activityCardController.setActivity(activity);
            activityCardController.setNode(hbox);
            activityCardController.setDay(selectedDate);
            activityCardController.setBuildItineraryParentController(this);
            totalRows++;
            addedGrid.addRow(selectedRow + 1, hbox);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addRowBelow(GridPane gridPane, int rowIndex, Activity activity) {
        int numRows = gridPane.getRowCount();

        for (int row = numRows - 1; row > rowIndex; row--) {
            Node node = getNodeFromGridPane(gridPane, row);
            if (node != null) {
                GridPane.setRowIndex(node, row + 1);
            }
        }
        loadToAddedGrid(activity);
    }

    public Node getNodeFromGridPane(GridPane gridPane, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public boolean addToTrip(Activity activity){
//        System.out.println("TOTAL: " + totalRows + "   DATE " + selectedDate + "   ROW" + selectedRow);
        if(totalRows >= 0 && selectedRow >= 0){
            addRowBelow(addedGrid, selectedRow, activity);
            saveItineraryButton.setVisible(true);
            if(dailyActivities.get(selectedDate) == null){
                Set<Activity> activitySet = new HashSet<>();
                activitySet.add(activity);
                dailyActivities.set(selectedDate, activitySet);
            }else{
                dailyActivities.get(selectedDate).add(activity);
            }
            totalRows++;
            return true;
        }
        return false;
    }

    public void setDayInformation(int day, int row, DayCardController dayCardController){
        if(savedDayCard != null){
            savedDayCard.resetAddToDayButton();
        }
        selectedDate = day;
        selectedRow = row;
        savedDayCard = dayCardController;
    }

    public void removeFromTrip(Activity activity, int day){
        System.out.println("DATE: " + selectedDate + "ROW: " + selectedRow + "TOTAL: " + totalRows);
        dailyActivities.get(day).remove(activity);
        totalRows--;
        System.out.println("DATE: " + selectedDate + "ROW: " + selectedRow + "TOTAL: " + totalRows);
        loadToSelectedGrid(activity);
        System.out.println("DATE: " + selectedDate + "ROW: " + selectedRow + "TOTAL: " + totalRows);
    }

    public void addDay(){
        Set<Activity> todaysActivities = new HashSet<>();
        dailyActivities.add(todaysActivities);
        totalRows++;
        loadDayToAddedGrid(dailyActivities.size() - 1);
    }

    private void loadDayToAddedGrid(int day){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/dayCard.fxml"));

        try {
            HBox hbox = fxmlLoader.load();
            DayCardController dayCardController = fxmlLoader.getController();
            dayCardController.setDay(day);
            dayCardController.setRow(totalRows);
            dayCardController.setNode(hbox);
            dayCardController.setBuildItineraryParentController(this);
            addedGrid.addRow(totalRows, hbox);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void handleSaveItineraryButton(ActionEvent event){
        if(!nameField.getText().isEmpty()){
            trip.setTitle(nameField.getText());
        }
        Set<Activity> activitySet = trip.getActivities().get(0);
        int count = 0;
        for(Activity activity : activitySet){
            if(count == 0){
                String url = PlanNextTripController.getTripImageURL(activity.getCountry());
                trip.setUrlToFirstImage(url);
                if(trip.getTitle() ==  null || trip.getTitle().isEmpty()){
                    trip.setTitle(activity.getCountry());
                }
            }
            count++;
        }
        trip.setBudget(String.format("%.2f", Double.parseDouble(budgetField.getText())));
        trip.setLength(String.valueOf(trip.getActivities().size()));
        saveItineraryButton.setVisible(false);
        FirestoreAction.addTrip(trip);
        loadFinalTripPage(event);
    }

    private void loadFinalTripPage(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/travel/virtualtravelassistant/finalTripPage.fxml"));
            Parent parent = loader.load();
            FinalTripPageController finalTripPageController = loader.getController();
            finalTripPageController.setTrip(trip);
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
