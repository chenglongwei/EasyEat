package com.easyeat.bean;

import java.io.Serializable;

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
    public double price;
}
