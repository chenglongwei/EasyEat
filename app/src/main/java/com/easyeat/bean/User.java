package com.easyeat.bean;

import java.io.Serializable;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class User implements Serializable {
    public long userId;
    public String email;
    public String username;
    public String phonenumber;
    public String address;
    public Payment[] paymentInfo;
}
