package com.example.sarah.nav;

public class ModelFood {
    private String index1, index2, price;
    public String getIndex1() {
        return index1;
    }

    public void setIndex1(String index1) {
        this.index1 = index1;
    }

    public String getIndex2() {
        return index2;
    }

    public void setIndex2(String index2) {
        this.index2 = index2;
    }

    public String getPrice() {
        return price;
    }

    public void changeText(String text){
        this.index2 = text;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public ModelFood(String index1, String index2, String price) {
        this.index1 = index1;
        this.index2 = index2;
        this.price = price;
    }

}
