package com.easyeat.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class Restaurant implements Serializable {
    public long id;
    public String name;
    public String address;
    public String description;
    public String url;
    public double distance;
    public String opentime;
    public String phonenumber;
    public boolean isfavorite;
    public Menu[] menu;
    public String[] slots;

    public String formatDistance() {
        if (Math.abs(distance) < 0.01) {
            return "";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(distance * 6.21e-4) + "mi";
    }
}