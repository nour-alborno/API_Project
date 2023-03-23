package com.example.apinouralborno.Model;

public class Order {
    private String Picture;
    private String Title;
    private String Quantity;
    private String Price;
    private String documentId;

    public Order(String picture, String title, String quantity, String price, String documentId) {
        Picture = picture;
        Title = title;
        Quantity = quantity;
        Price = price;
        this.documentId = documentId;
    }

    public Order(String picture, String title, String quantity, String price) {
        Picture = picture;
        Title = title;
        Quantity = quantity;
        Price = price;
    }

    public Order() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
