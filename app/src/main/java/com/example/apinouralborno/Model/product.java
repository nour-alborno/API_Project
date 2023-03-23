package com.example.apinouralborno.Model;

import java.io.Serializable;

public class product implements Serializable {
    private String Title ="";
    private String Category="";
    private String Details="";
    private String Picture="";
    private double Price=0;
    private String documentID;

    public product() {
    }

    public product(String title, String category, String details, String picture, double price) {
        Title = title;
        Category = category;
        Details = details;
        Picture = picture;
        Price = price;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
