package com.travel.virtualtravelassistant.FriendsList;

import com.travel.virtualtravelassistant.FinalTripPageController;
import com.travel.virtualtravelassistant.User.UserInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class FriendCardController {
    @FXML
    ImageView imageView;

    @FXML
    Label fullName;

    @FXML
    Label email;

    @FXML
    Button inviteButton;

    private UserInfo friend;
    private FinalTripPageController controller;

    public void setFriend(UserInfo friend){
        this.friend = friend;
        imageView.setImage(friend.getProfile_picture());
        fullName.setText(friend.getFullName());
        email.setText(friend.getUID());
    }


    public void setSmallCardAddedFriend(UserInfo friend) {
        this.friend = friend;
        inviteButton.setVisible(true);
        inviteButton.setDisable(true);
        inviteButton.setOpacity(1.0);
        inviteButton.setText("Added!");
        imageView.setImage(friend.getProfile_picture());
        fullName.setText(friend.getFullName());
        email.setText(friend.getUID());
    }

    public void setSmallCardInviteFriend(UserInfo friend) {
        this.friend = friend;
        inviteButton.setVisible(true);
        imageView.setImage(friend.getProfile_picture());
        fullName.setText(friend.getFullName());
        email.setText(friend.getUID());
    }

    public void setController(FinalTripPageController controller){
        this.controller = controller;
    }

    public void handleInviteButton(){
        controller.inviteFriend(friend);
        inviteButton.setDisable(true);
        inviteButton.setOpacity(1.0);
        inviteButton.setText("Invited!");
    }
}
