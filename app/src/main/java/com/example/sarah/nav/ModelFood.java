package com.example.sarah.nav;

public class ModelFood {
    private String restaurant_name, category, price,imgname,rid;

    public String getRestaurant_name() { return restaurant_name; }
    public String getCategory() { return category; }
    public String getImgname() { return imgname; }
    public String getPrice() {
        return price;
    }
    public String getRid(){return rid;}

//    public void setPrice(String price) {
//        this.price = price;
//    }

    public ModelFood(String restaurant_name, String category, String imgname,String price, String rid) {
        this.restaurant_name = restaurant_name;
        this.category = category;
        this.imgname = imgname;
        this.price = price;
        this.rid = rid;
    }
}

