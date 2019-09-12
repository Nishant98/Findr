package com.example.sarah.nav;

public class Data {

//    private String description;
//    private String index1;
//    private String index2;
//    private String imagePath;
    private String restaurant_name;
    private String category;
    private String imgname;
    private String price;
    private String rid;

    Data(String restaurant_name, String category, String imgname, String price, String rid) {
        this.restaurant_name = restaurant_name;
        this.category = category;
        this.imgname = imgname;
        this.price = price;
        this.rid = rid;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }
    public String getCategory(){return category;}

    public String getImgname() { return imgname; }

    public String getPrice() { return price; }

    public String getRid() {return rid; }
    //    String getImagePath() {
//        return imagePath;
//    }
//
//    public String getIndex1() {
//        return index1;
//    }
//
//    public String getIndex2() {
//        return index2;
//    }

}