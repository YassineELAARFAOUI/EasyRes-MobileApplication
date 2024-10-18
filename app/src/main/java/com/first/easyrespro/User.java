package com.first.easyrespro;

public class User {
    public String firstName,lastName,phoneN,email,password,city,address,entreprise,description,imageProfile;
    public User(){


    }
    public User(String firstName,String lastName){
        this.firstName= firstName;
        this.lastName =lastName;

    }

    public User(String firstName,String lastName,String phoneN,String email,String password,String city,String address,String entreprise,String description,String imageProfile){
        this.firstName= firstName;
        this.lastName =lastName;
        this.phoneN= phoneN;
        this.email =email;
        this.password =password;
        this.city= city;
        this.address =address;
        this.entreprise= entreprise;
        this.description =description;
        this.imageProfile=imageProfile;
    }

    public User(String firstName,String lastName,String email,String password){
        this.firstName= firstName;
        this.lastName =lastName;
        this.email =email;
        this.password =password;

    }
}
