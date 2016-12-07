package com.easyeat;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class Config {
    public static final String TAG = "EasyEat";
//    private static final String HOST = "https://private-ec7ca6-cmpe295.apiary-mock.com";
    private static final String AUTH_HOST = "http://35.163.147.127:8080";
    private static final String HOST = AUTH_HOST;

    public static final String key_session_id = "session_id";
    public static final String key_user_id = "user_id";
    public static final String key_user = "user";
    public static final String key_latitude = "latitude";
    public static final String key_longtitude = "longtitude";
    public static final String key_restaurant = "restaurant";
    public static final String key_message = "message";
    public static final String key_status = "status";
    public static final String OK = "OK";
    public static final String key_data = "data";
    public static final String key_query = "query";
    public static final String key_accessToken = "accessToken";
    public static final String key_reservations = "reservations";

    // position information
    public static double latitude = 0.0;
    public static double longtitude = 0.0;

    // URL
    public static final String HTTP_GET_RESTAURANT = HOST + "/restaurant";
    public static final String HTTP_GET_FAVORITES_RESTAURANT = HOST + "/favorite";
    public static final String HTTP_POST_TABLE_RESERVE = HOST + "/table/reserve";
    public static final String HTTP_GET_TABLE_RESERVE = HOST + "/table/reserve";
    public static final String HTTP_GET_ORDER_TAKEOUT = HOST + "/order/takeout";

    public static final String HTTP_POST_REGISTER = AUTH_HOST + "/user/register";
    public static final String HTTP_POST_SIGN_IN = AUTH_HOST + "/user/login";
    public static final String HTTP_POST_UPDATE_PROFILE = AUTH_HOST + "/user/userid";

    public static final String salt = "7i6UbiQXBRffBpmn%&V$(b9s_GXA(4KU";
    public static final String key_menu = "menu";
    public static final String key_slots = "slots";
}
