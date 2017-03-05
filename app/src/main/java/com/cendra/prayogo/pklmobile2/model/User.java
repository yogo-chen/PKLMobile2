package com.cendra.prayogo.pklmobile2.model;

/**
 * Created by Admin on 3/2/2017.
 */

public class User {
    public final String sid;
    public final String email;
    public final String birthday;
    public final String name;
    public final String address;
    public final String phone;
    public final String featuredProduct;

    public User(String sid, String email, String birthday, String name, String address, String phone, String featuredProduct) {
        this.sid = sid;
        this.email = email;
        this.birthday = birthday;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.featuredProduct = featuredProduct;
    }

    public User(String email, String birthday, String name, String address, String phone, String featuredProduct) {
        this(null, email, birthday, name, address, phone, featuredProduct);
    }
}
