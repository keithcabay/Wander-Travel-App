package com.travel.virtualtravelassistant.Inbox;

import com.travel.virtualtravelassistant.FinalTripPageController;
import com.travel.virtualtravelassistant.Inbox.Notification;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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

    @FXML
    Button acceptButton;
    @FXML
    Button denyButton;
    @FXML
    Button viewTripButton;

    private Node node;

    public void setNotification(Notification notification){
        this.notification = notification;
        if(notification.getTripInfo() != null){
            viewTripButton.setVisible(true);
        }
        senderNameLabel.setText(notification.getSenderFirstName() + " " + notification.getSenderLastName());
        senderEmailLabel.setText(notification.getSenderEmail());
        typeLabel.setText(notification.getNotification_type());
        messageLabel.setText(notification.getMessage());

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/travel/virtualtravelassistant/Images/trash-bin.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        denyButton.setGraphic(imageView);

        Image checkImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/travel/virtualtravelassistant/Images/check-icon.png")));
        ImageView checkImageView = new ImageView(checkImage);
        checkImageView.setFitHeight(20);
        checkImageView.setFitWidth(20);
        acceptButton.setGraphic(checkImageView);
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

    public void handleViewTripButton(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/travel/virtualtravelassistant/finalTripPage.fxml"));
            Parent parent = loader.load();
            FinalTripPageController finalTripPageController = loader.getController();
            finalTripPageController.loadFriendTrip(notification);
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void isTripNotification(boolean isTripNoti){
        if(isTripNoti){
            acceptButton.setOnAction(e -> handleAddTripButton());
            denyButton.setOnAction(e -> handleDenyTripButton());
        }
    }

    public void handleAddTripButton(){
        Trip trip = FirestoreAction.getFriendTrip(notification);
        FirestoreAction.addFriendToTrip(notification.getTripInfo(), notification.getSenderEmail(), notification.getReceiverEmail());
        FirestoreAction.addTrip(trip);
        FirestoreAction.deleteNotification(notification);
        deleteMe();
    }

    public void handleDenyTripButton(){
        FirestoreAction.deleteNotification(notification);
    }
}
