package com.example.sarah.nav;

public class Data {

    private String description;
    private String index1;
    private String index2;
    private String imagePath;

    Data(String imagePath, String index1, String index2, String description) {
        this.imagePath = imagePath;
        this.description = description;
        this.index1 = index1;
        this.index2 = index2;
    }

    public String getDescription() {
        return description;
    }

    String getImagePath() {
        return imagePath;
    }

    public String getIndex1() {
        return index1;
    }

    public String getIndex2() {
        return index2;
    }

}