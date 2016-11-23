package com.easyeat.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.easyeat.BuildConfig;
import com.easyeat.Config;
import com.easyeat.EasyEatApplication;
import com.easyeat.bean.User;
import com.google.gson.Gson;

/**
 * Created by chenglongwei on 11/22/16.
 */
public class SharedPrefsUtil {
    private static final String prefix = BuildConfig.DEBUG ? "d_" : "r_";
    public static final String shared_prefs = "easy_eat";
    private static final String key_se_user = "se_user";

    public static void saveEncryptUser(User user) {
        String jsonUser = new Gson().toJson(user);
        String se_user = AesUtil.encrypt(jsonUser, EasyEatApplication.deviceGuid);
        Log.d(Config.TAG, "encrypt string: " + se_user);
        putString(key_se_user, se_user);
    }

    public static User loadEncryptUser() {
        String se_user = getString(key_se_user);
        if (TextUtils.isEmpty(se_user)) {
            return null;
        }
        String jsonUser = AesUtil.decrypt(se_user, EasyEatApplication.deviceGuid);
        User user = new Gson().fromJson(jsonUser, User.class);
        return user;
    }

    public static String getString(String key) {
        SharedPreferences sp = EasyEatApplication.instance.getApplicationContext().getSharedPreferences(shared_prefs, Activity.MODE_PRIVATE);
        return sp.getString(prefix + key, "");
    }

    public static void putString(String key, String value) {
        SharedPreferences sp = EasyEatApplication.instance.getApplicationContext().getSharedPreferences(shared_prefs, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(prefix + key, value);
        editor.apply();
    }

    //logout，清空用户数据和session_id
    public static void deleteUser() {
        deleteValues(key_se_user, Config.key_session_id);
    }

    public static void deleteValues(String... keys) {
        if (keys == null || keys.length == 0)
            return;
        SharedPreferences sp = EasyEatApplication.instance.getApplicationContext().getSharedPreferences(shared_prefs, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (String key : keys) {
            editor.remove(prefix + key);
            Log.d(Config.TAG, "delete " + key);
        }
        editor.apply();
    }
}
