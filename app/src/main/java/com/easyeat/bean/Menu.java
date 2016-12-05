package com.easyeat.bean;

import java.io.Serializable;

/**
 * Created by chenglongwei on 12/5/16.
 */

public class Menu implements Serializable {
    public long id;
    public String name;
    public String description;
    public String image;
    public double price;
    public String status;
}
