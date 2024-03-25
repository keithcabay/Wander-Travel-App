package com.travel.virtualtravelassistant.User;


public class UserInfo {
    private String UID;

   private String first_name;

   private String last_name;

   private String email;

   public UserInfo(String UID, String first_name, String last_name, String email){
        this.UID = UID;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
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
}
