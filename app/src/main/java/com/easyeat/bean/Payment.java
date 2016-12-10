package com.easyeat.bean;

import java.io.Serializable;

/**
 * Created by chenglongwei on 12/9/16.
 */

public class Payment implements Serializable {
    public long id;
    public String cardNumber;
    public String expiredMonth;
    public String expiredYear;
    public String cardHolder;
    public String address;
    public String zipcode;
    public String ccv;
}
