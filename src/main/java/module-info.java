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


    opens com.travel.virtualtravelassistant to javafx.fxml;
    exports com.travel.virtualtravelassistant;
}