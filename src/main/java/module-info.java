module com.travel.virtualtravelassistant {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.travel.virtualtravelassistant to javafx.fxml;
    exports com.travel.virtualtravelassistant;
}