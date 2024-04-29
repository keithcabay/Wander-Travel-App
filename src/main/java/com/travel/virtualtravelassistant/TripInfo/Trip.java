package com.travel.virtualtravelassistant.TripInfo;

import com.travel.virtualtravelassistant.Activity;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Map;

public class Trip {
    private Image image;

    private String imageSource;

    private String countryName;

    private String cityName;

    private double budget;

    private int length;

    //help filter activities at each location/city
    Map<String, List<Activity>> locationToActivities;

    //Each index in list is another day of activities
    List<List<Activity>> activities;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
