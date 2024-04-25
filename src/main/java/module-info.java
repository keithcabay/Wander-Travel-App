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
    requires com.google.gson;
    requires okhttp3;
    requires okhttp3.logging;
    requires org.json;


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
    exports com.travel.virtualtravelassistant.PlanNextTrip;
    opens com.travel.virtualtravelassistant.PlanNextTrip to javafx.fxml;
    exports com.travel.virtualtravelassistant.FriendsList;
    opens com.travel.virtualtravelassistant.FriendsList to javafx.fxml;
    exports com.travel.virtualtravelassistant.Inbox;
    opens com.travel.virtualtravelassistant.Inbox to javafx.fxml;

}