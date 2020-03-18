package com.example.impiled_students;

public class SellersClass {
    private String avail,location,product_des,product_name,product_pic,seller_name,seller_pic;

    public SellersClass(){}

    public SellersClass(String avail, String location, String product_des, String product_name, String product_pic, String seller_name, String seller_pic) {
        this.avail = avail;
        this.location = location;
        this.product_des = product_des;
        this.product_name = product_name;
        this.product_pic = product_pic;
        this.seller_name = seller_name;
        this.seller_pic = seller_pic;
    }

    public String getAvail() {
        return avail;
    }

    public void setAvail(String avail) {
        this.avail = avail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProduct_des() {
        return product_des;
    }

    public void setProduct_des(String product_des) {
        this.product_des = product_des;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_pic() {
        return product_pic;
    }

    public void setProduct_pic(String product_pic) {
        this.product_pic = product_pic;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_pic() {
        return seller_pic;
    }

    public void setSeller_pic(String seller_pic) {
        this.seller_pic = seller_pic;
    }
}
