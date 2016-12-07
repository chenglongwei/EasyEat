package com.easyeat.bean;

import java.io.Serializable;

/**
 * Created by chenglongwei on 12/6/16.
 */

public class ReservationInfo implements Serializable {
    public Restaurant restaurant;
    public Reservation reservation;
    public MenuQuality[] menus;
}


