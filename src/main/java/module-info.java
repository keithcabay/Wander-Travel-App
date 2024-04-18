module com.travel.virtualtravelassistant {
    requires javafx.controls;
    requires javafx.fxml;
    requires google.api.client;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires google.cloud.firestore;
    requires google.cloud.core;
    requires com.google.api.apicommon;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires google.cloud.storage;
    requires javafx.web;


    opens com.travel.virtualtravelassistant to javafx.fxml;
    exports com.travel.virtualtravelassistant;
    exports com.travel.virtualtravelassistant.User;
    opens com.travel.virtualtravelassistant.User to javafx.fxml;
    exports com.travel.virtualtravelassistant.TripInfo;
    opens com.travel.virtualtravelassistant.TripInfo to javafx.fxml;
    exports com.travel.virtualtravelassistant.AuthenticationControllers;
    opens com.travel.virtualtravelassistant.AuthenticationControllers to javafx.fxml;
    exports com.travel.virtualtravelassistant.AlbumControllers;
    opens com.travel.virtualtravelassistant.AlbumControllers to javafx.fxml;
}