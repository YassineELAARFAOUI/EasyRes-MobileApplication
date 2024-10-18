package com.first.easyrespro;

public class Post {

    public String pid,description,price,city,address,img1,img2,img3,img4,email,latitude,longitude,Catigorie,star,date;

    public Post(String pid,String description,String price,String city,String address,String img1,String img2,String img3,String img4,String email,String latitude,String longitude,String Catigorie,String star,String date) {
        this.pid = pid;
        this.description = description;
        this.price = price;
        this.city = city;
        this.address = address;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.Catigorie = Catigorie;
        this.star = star;
        this.date=date;

    }

    public Post(String pid,String star,String img1,String description,String city,String price) {
        this.pid = pid;
        this.description = description;
        this.price = price;
        this.city = city;
        this.img1 = img1;
        this.star = star;
    }

}
