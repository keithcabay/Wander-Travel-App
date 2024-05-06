package com.travel.virtualtravelassistant.Inbox;

import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

public class InboxController {

    @FXML
    GridPane notificationsGrid;

    @FXML
    Label inboxTitleLabel;

    private int currGridColumn = 1;

    private int currGridRow = 1;

    public void initialize(){
        List<Notification> notifications = FirestoreAction.getNotifications();
        inboxTitleLabel.setText("You have " + notifications.size() + " new notifications.");

        for(Notification noti : notifications){
            loadNotifications(noti);
        }
    }

    private void loadNotifications(Notification notification){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/notificationCard.fxml"));

        try {
            BorderPane borderPane = fxmlLoader.load();
            NotificationCardController notificationCardController = fxmlLoader.getController();
            notificationCardController.setNotification(notification);
            notificationCardController.setNode(borderPane);
            if(notification.tripInfo != null){
                notificationCardController.isTripNotification(true);
            }
            notificationsGrid.add(borderPane, currGridColumn, currGridRow++);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
