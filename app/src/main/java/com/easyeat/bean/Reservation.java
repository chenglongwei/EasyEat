package com.easyeat.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by chenglongwei on 12/6/16.
 */

public class Reservation implements Serializable {
    public long id;
    public String timeSlot;
    public int people;
    public String date;
    public boolean isPrivate;
    public boolean takeOut;
    private double price;
    public String status;

    public String formatPrice() {
        DecimalFormat df = new DecimalFormat("#.00");
        return "Price: " + "$" + df.format(price);
    }
}
