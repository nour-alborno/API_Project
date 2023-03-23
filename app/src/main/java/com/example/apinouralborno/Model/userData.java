package com.example.apinouralborno.Model;

public class userData {
    private String name;
    private String email;
    private int country;
    private String Picture;

    public userData() {
    }

    public userData(String name, String email, int country, String picture) {
        this.name = name;
        this.email = email;
        this.country = country;
        Picture = picture;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

}
