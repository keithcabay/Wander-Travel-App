package com.travel.virtualtravelassistant.FriendsList;

import com.travel.virtualtravelassistant.Inbox.Notification;
import com.travel.virtualtravelassistant.User.CurrentUser;
import com.travel.virtualtravelassistant.User.UserInfo;
import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Set;

public class FriendsListController {
    @FXML
    GridPane friendsGrid;

    @FXML
    Label title;

    public int currGridColumn;
    public int currGridRow;

    public void initialize(){
        Set<UserInfo> friends = FirestoreAction.getFriends();
        if(friends.size() == 1){
            title.setText("1 friend.");
        }else{
            title.setText(friends.size() + " friends.");
        }

        for (UserInfo friend : friends){
            loadFriends(friend);
        }
    }

    public void handleAddFriendButton(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/travel/virtualtravelassistant/friendRequestForm.fxml"));
        FriendRequestFormController friendRequestFormController;
        try {
            BorderPane borderPane = loader.load();
            friendRequestFormController = loader.getController();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Friend Request Form");
            popupStage.setScene(new Scene(borderPane));
            friendRequestFormController.setStage(popupStage);

            popupStage.showAndWait();

            String receiverEmail = friendRequestFormController.getReceiverEmail();
            String message = friendRequestFormController.getMessage();

            if(receiverEmail.isEmpty()){
                return;
            }

            Notification notification = new Notification();
            notification.setReceiverEmail(receiverEmail.toLowerCase());
            notification.setMessage(message);
            notification.setNotification_type("Friend Request");
            notification.setSenderEmail(CurrentUser.getInstance().getUserInfo().getUID());
            notification.setSenderFirstName(CurrentUser.getInstance().getUserInfo().getFirst_name());
            notification.setSenderLastName(CurrentUser.getInstance().getUserInfo().getLast_name());

            FirestoreAction.sendNotification(notification);
        }catch (Exception e){

        }
    }

    private void loadFriends(UserInfo friend){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/friendCard.fxml"));

        try {
            BorderPane borderPane = fxmlLoader.load();
            FriendCardController friendCardController = fxmlLoader.getController();
            friendCardController.setFriend(friend);
            friendsGrid.add(borderPane, currGridColumn, currGridRow++);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
