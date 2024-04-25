package com.travel.virtualtravelassistant.FriendsList;

import com.travel.virtualtravelassistant.User.UserInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class FriendCardController {
    @FXML
    ImageView imageView;

    @FXML
    Label fullName;

    @FXML
    Label email;

    private UserInfo friend;

    public void setFriend(UserInfo friend){
        this.friend = friend;
        imageView.setImage(friend.getProfile_picture());
        fullName.setText(friend.getFullName());
        email.setText(friend.getUID());
    }


}
