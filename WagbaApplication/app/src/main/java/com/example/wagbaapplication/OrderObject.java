package com.example.wagbaapplication;

public class OrderObject {
    private String DishName;
    private String Quantity;
    private String Price;
    private String Image;

    public OrderObject(String dishName, String quantity, String price, String image) {
        DishName = dishName;
        Quantity = quantity;
        Price = price;
        Image = image;
    }

    public OrderObject() {
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
