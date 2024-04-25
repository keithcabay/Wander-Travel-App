package com.travel.virtualtravelassistant.Inbox;

import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class NotificationCardController {
    Notification notification;

    @FXML
    Label senderNameLabel;

    @FXML
    Label senderEmailLabel;

    @FXML
    Label typeLabel;

    @FXML
    Label messageLabel;

    private Node node;

    public void setNotification(Notification notification){
        this.notification = notification;
        senderNameLabel.setText(notification.getSenderFirstName() + " " + notification.getSenderLastName());
        senderEmailLabel.setText(notification.getSenderEmail());
        typeLabel.setText(notification.getNotification_type());
        messageLabel.setText(notification.getMessage());
    }

    public void setNode(Node node){
        this.node = node;
    }

    public void handleAcceptFriendRequest(){
        FirestoreAction.addFriend(notification);
        FirestoreAction.deleteNotification(notification);
        deleteMe();
    }

    public void handleDenyFriendRequest(){
        FirestoreAction.deleteNotification(notification);
        deleteMe();
    }

    private void deleteMe(){
        GridPane gridPane = (GridPane) node.getParent();
        gridPane.getChildren().remove(node);
    }

}
