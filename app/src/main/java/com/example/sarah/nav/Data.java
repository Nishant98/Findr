package com.example.sarah.nav;

public class Data {

    private String description;
    private String restaurant_name;
    private String category;
    private String imgname;
    private String price;
    private String rid;
    private String time;

    Data(String restaurant_name, String category, String imgname, String price, String description,String rid, String time) {
        this.restaurant_name = restaurant_name;
        this.category = category;
        this.imgname = imgname;
        this.price = price;
        this.description=description;
        this.rid = rid;
        this.time = time;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }
    public String getRid(){ return rid; }
    public String getDescription() { return description; }
    public String getCategory(){return category;}
    public String getImgname() { return imgname; }
    public String getPrice() { return price; }
    public String getTime() {return time;}
}