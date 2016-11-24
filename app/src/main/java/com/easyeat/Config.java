package com.easyeat;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class Config {
    public static final String TAG = "EasyEat";
    private static final String HOST = "https://private-ec7ca6-cmpe295.apiary-mock.com";

    public static final String key_session_id = "session_id";
    public static final String key_user_id = "user_id";
    public static final String key_latitude = "latitude";
    public static final String key_longtitude= "longtitude";
    public static final String key_restaurant = "restaurant";
    public static final String key_message = "message";
    public static final String key_status = "status";
    public static final String OK = "OK";
    public static final String key_data = "data";

    // position information
    public static double latitude = 0.0;
    public static double longtitude = 0.0;

    // URL
    public static final String HTTP_GET_RESTAURANT = HOST + "/restaurant";
}
