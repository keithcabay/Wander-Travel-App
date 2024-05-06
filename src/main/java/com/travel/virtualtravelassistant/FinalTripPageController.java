package com.travel.virtualtravelassistant;

import com.travel.virtualtravelassistant.FriendsList.FriendCardController;
import com.travel.virtualtravelassistant.Inbox.Notification;
import com.travel.virtualtravelassistant.TripInfo.Trip;
import com.travel.virtualtravelassistant.User.CurrentUser;
import com.travel.virtualtravelassistant.User.UserInfo;
import com.travel.virtualtravelassistant.Utility.FirestoreAction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class FinalTripPageController {
    @FXML
    HBox banner;
    @FXML
    Label lengthLabel;
    @FXML
    Label budgetLabel;
    @FXML
    GridPane activitiesGrid;
    @FXML
    GridPane friendsGrid;

    private Trip trip;
    private final int currGridColumn = 0;
    private int currGridRow = 0;

    private final int friendCurrGridColumn = 0;
    private int friendCurrGridRow = 0;

    private Set<UserInfo> friends;

    public void initialize(){
        Label label = new Label("Invite Friends to Your Trip");
        label.setStyle("-fx-font-size: 25;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10px;");

        friendsGrid.add(label, friendCurrGridColumn, friendCurrGridRow++);
    }

    public void loadTripFromDB(Trip trip){
        this.trip = trip;

        budgetLabel.setText("$" + trip.getBudget());
        lengthLabel.setText(trip.getLength() + "Days");

        FirestoreAction.getActivities(trip);
        friends = FirestoreAction.getFriends();
        Set<String> addedFriends = trip.getFriends();
        for(UserInfo friend : friends){
            if(addedFriends != null && addedFriends.contains(friend.getUID())){
                loadAddedFriend(friend);
            }else{
                loadInviteFriend(friend);
            }
        }
        loadBanner();
        loadActivities();
    }

    public void setTrip(Trip trip){
        this.trip = trip;
        budgetLabel.setText(trip.getBudget());
        lengthLabel.setText(trip.getLength());

        friends = FirestoreAction.getFriends();
        Set<String> addedFriends = trip.getFriends();
        for(UserInfo friend : friends){
            if(addedFriends != null && addedFriends.contains(friend.getUID())){
                loadAddedFriend(friend);
            }else{
                loadInviteFriend(friend);
            }
        }
        loadBanner();
        loadActivities();

    }

    private void loadBanner(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/largeTripPreviewBanner.fxml"));

        try {
            ScrollPane pane = fxmlLoader.load();
            LargeTripPreviewBannerController largeTripPreviewBannerController = fxmlLoader.getController();
            largeTripPreviewBannerController.setTrip(trip);
            banner.getChildren().add(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadActivities(){
        List<Set<Activity>> activities = trip.getActivities();

        int day = 1;
        for(Set<Activity> activitySet : activities){
            Label label = new Label("Day " + day);
            label.setStyle("-fx-font-size: 25;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 10px;");

            activitiesGrid.add(label, currGridColumn, currGridRow++);
            for(Activity activity : activitySet) {
                loadToGrid(activity);
            }
            day++;
        }
    }

    private void loadToGrid(Activity activity){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/activityCard.fxml"));

        try {
            HBox hbox = fxmlLoader.load();
            ActivityCardController activityCardController = fxmlLoader.getController();
            activityCardController.setActivity(activity);
            activityCardController.setNode(hbox);
            activityCardController.setFinalTrip(true);
            activitiesGrid.add(hbox, currGridColumn, currGridRow++);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAddedFriend(UserInfo friend){
        System.out.println("Load Add friend");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/smallFriendCard.fxml"));

        try {
            BorderPane borderPane = fxmlLoader.load();
            FriendCardController friendCardController = fxmlLoader.getController();
            friendCardController.setSmallCardAddedFriend(friend);
            friendsGrid.add(borderPane, friendCurrGridColumn, friendCurrGridRow++);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadInviteFriend(UserInfo friend){
        System.out.println("Load invite friend");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/travel/virtualtravelassistant/smallFriendCard.fxml"));

        try {
            BorderPane borderPane = fxmlLoader.load();
            FriendCardController friendCardController = fxmlLoader.getController();
            friendCardController.setSmallCardInviteFriend(friend);
            friendCardController.setController(this);
            friendsGrid.add(borderPane, friendCurrGridColumn, friendCurrGridRow++);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void inviteFriend(UserInfo friend){
        Notification notification = new Notification();
        notification.setNotification_type("Trip Invite");
        notification.setTripInfo(trip.getTripId());
        notification.setMessage("Join " + CurrentUser.getInstance().getUserInfo().getFirst_name() + "'s trip!");
        notification.setSenderEmail(CurrentUser.getInstance().getUserInfo().getUID());
        notification.setReceiverEmail(friend.getUID());
        notification.setSenderFirstName(CurrentUser.getInstance().getUserInfo().getFirst_name());
        notification.setSenderLastName(CurrentUser.getInstance().getUserInfo().getLast_name());

        FirestoreAction.sendNotification(notification);
    }

    public void loadFriendTrip(Notification notification) {
        trip = FirestoreAction.getFriendTrip(notification);
        budgetLabel.setText(trip.getBudget());
        lengthLabel.setText(trip.getLength());
        loadBanner();
        loadActivities();
        Set<String> friends = trip.getFriends();
        if(friends != null && !friends.isEmpty()){
            for(String string : friends){
                loadInviteFriend(FirestoreAction.getUserInfo(string));
            }
        }

    }
}
