package com.travel.virtualtravelassistant.User;


import javafx.scene.image.Image;

public class UserInfo {
    private String UID;

   private String first_name;

   private String last_name;

   private String location;

   private String bio;

   private String email;

   private Image profile_picture;

   public UserInfo(){}

   //registration
    public UserInfo(String UID, String first_name, String last_name, String email){
        this.UID = UID;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    //login
   public UserInfo(String UID, String first_name, String last_name, String email, String location, String bio){
        this.UID = UID;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.location = location;
        this.bio = bio;
   }

   public String getFullName(){
       return first_name + " " + last_name;
   }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Image getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(Image profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
