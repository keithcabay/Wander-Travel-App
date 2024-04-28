package com.travel.virtualtravelassistant;

import java.util.List;

public class Activity {
    private String location_id;

    private String name;

    private String urlToFirstImage;

    private String description;

    private String date;

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlToFirstImage() {
        return urlToFirstImage;
    }

    public void setUrlToFirstImage(String urlToFirstImage) {
        this.urlToFirstImage = urlToFirstImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
