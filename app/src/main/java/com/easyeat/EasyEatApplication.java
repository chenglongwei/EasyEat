package com.easyeat;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.VolleyLog;
import com.easyeat.bean.User;
import com.easyeat.http.RequestManager;
import com.easyeat.util.SharedPrefsUtil;
import com.easyeat.util.UniqueDeviceIDFactory;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class EasyEatApplication extends Application {
    public static EasyEatApplication instance;
    public static String deviceGuid;

    private static User currentUser;
    private static String session_id;

    @Override
    public void onCreate() {
        super.onCreate();
        deviceGuid = UniqueDeviceIDFactory.getUniqueDeviceID(getApplicationContext());
        instance = this;
        initVolley();
    }

    private void initVolley() {
        VolleyLog.setTag("EasyEat");
        VolleyLog.DEBUG = BuildConfig.DEBUG;
        String userAgent = "volley/0 Android EasyEat/" + BuildConfig.VERSION_NAME;
        RequestManager.init(getApplicationContext());
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            currentUser = SharedPrefsUtil.loadEncryptUser();
        }
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
        if (currentUser != null) {
            SharedPrefsUtil.saveEncryptUser(currentUser);
        }
    }

    public static String getSessionId() {
        if (!TextUtils.isEmpty(session_id)) {
            return session_id;
        }
        //使用保存过的session_id
        session_id = SharedPrefsUtil.getString(Config.key_session_id);
        if (!TextUtils.isEmpty(session_id)) {
            return session_id;
        }

        return null;
    }

    public static void setSessionId(String session_id) {
        EasyEatApplication.session_id = session_id;
        SharedPrefsUtil.putString(Config.key_session_id, session_id);
    }

    public static boolean isLogin() {
        try {
            User user = getCurrentUser();
            if (user != null && user.userId >= 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            SharedPrefsUtil.deleteUser();
            return false;
        }
    }
}
