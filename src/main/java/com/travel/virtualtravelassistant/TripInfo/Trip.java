package com.travel.virtualtravelassistant.TripInfo;

import com.travel.virtualtravelassistant.Activity;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Set;

public class Trip {
    private String tripId;
    private String title;
    private String urlToFirstImage;
    private Image image;
    private String budget;
    private String  length;
    private Set<String> friends;

    //Each index in list is another day of activities
    List<Set<Activity>> activities;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToFirstImage() {
        return urlToFirstImage;
    }

    public void setUrlToFirstImage(String urlToFirstImage) {
        this.urlToFirstImage = urlToFirstImage;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public List<Set<Activity>> getActivities() {
        return activities;
    }

    public void setActivities(List<Set<Activity>> activities) {
        this.activities = activities;
    }

    public Set<String> getFriends() {
        return friends;
    }

    public void setFriends(Set<String> friends) {
        this.friends = friends;
    }
}