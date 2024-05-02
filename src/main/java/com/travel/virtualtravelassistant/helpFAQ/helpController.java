package com.travel.virtualtravelassistant.helpFAQ;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class helpController {
    @FXML
    private Accordion faqAccordion;

    @FXML
    private TitledPane question1Pane;

    @FXML
    private TitledPane question2Pane;
    @FXML
    private TitledPane question3Pane;
    @FXML
    private TitledPane question4Pane;
    @FXML
    private TitledPane question5Pane;
    @FXML
    private TitledPane question6Pane;
    @FXML
    private TitledPane question7Pane;
    @FXML
    private TitledPane question8Pane;
    @FXML
    private TitledPane question9Pane;
    @FXML
    private TitledPane question10Pane;


    @FXML
    public void initialize() {
        faqAccordion.setExpandedPane(question1Pane);
    }

}