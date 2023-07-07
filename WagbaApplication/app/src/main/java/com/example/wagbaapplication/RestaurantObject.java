package com.example.wagbaapplication;


public class RestaurantObject {

    private String Name;
    private String Rating;
    private String Image;

    public RestaurantObject(String name, String rating, String image) {
        Name = name;
        Rating = rating;
        Image = image;
    }

    public RestaurantObject() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
