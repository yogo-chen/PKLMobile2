package com.cendra.prayogo.pklmobile2.model;

/**
 * Created by Admin on 3/2/2017.
 */

public class Transaction {
    public final String name;
    public final int soldPrice;
    public final int quantity;
    public final String date;

    public Transaction(String name, int soldPrice, int quantity, String date) {
        this.name = name;
        this.soldPrice = soldPrice;
        this.quantity = quantity;
        this.date = date;
    }
}
