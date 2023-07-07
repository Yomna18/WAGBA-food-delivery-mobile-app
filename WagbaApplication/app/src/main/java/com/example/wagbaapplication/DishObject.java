package com.example.wagbaapplication;

public class DishObject {

    private String Name;
    private String Price;
    private String Description;
    private String Image;
    private String DishId;
    private String Availability;

    public DishObject(String name, String price, String description, String image, String dishId, String availability) {
        Name = name;
        Price = price;
        Description = description;
        Image = image;
        DishId = dishId;
        Availability = availability;
    }

    public DishObject() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDishId() {
        return DishId;
    }

    public void setDishId(String dishId) {
        DishId = dishId;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

}
