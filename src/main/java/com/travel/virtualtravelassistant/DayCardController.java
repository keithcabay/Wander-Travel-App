package com.travel.virtualtravelassistant;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DayCardController {
    @FXML
    private Label dayLabel;
    @FXML
    private Button addToDayButton;
    private int day;
    private int row;
    private Node node;
    private BuildItineraryController buildItineraryController;

    public void setDay(int day){
        this.day = day;
        dayLabel.setText("DAY " + (day + 1));
    }

    public int getDay(){
        return  day;
    }

    public void setRow(int row){
        this.row = row;
    }
    public int getRow(){
        return row;
    }
    public void setNode(Node node){
        this.node = node;
    }

    public void setBuildItineraryParentController(BuildItineraryController buildItineraryController){
        this.buildItineraryController = buildItineraryController;
    }

    public void handleAddToDayButton(){
        buildItineraryController.setDayInformation(day, GridPane.getRowIndex(node),this);
        addToDayButton.setDisable(true);
        addToDayButton.setOpacity(1);
        addToDayButton.setText("Adding here.");
        addToDayButton.setStyle("-fx-background-color: #4d5e52;");
    }

    public void resetAddToDayButton(){
        addToDayButton.setText("Add to day");
        addToDayButton.setStyle("-fx-background-color: #97c297;");
        addToDayButton.setDisable(false);
    }
}
