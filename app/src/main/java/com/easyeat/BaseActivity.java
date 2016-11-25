package com.easyeat;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.easyeat.http.RequestManager;
import com.easyeat.util.Log;
import com.easyeat.util.Md5Util;

import org.json.JSONObject;

/**
 * Created by chenglongwei on 11/23/16.
 */

public class BaseActivity extends AppCompatActivity {
    protected Handler handler;
    protected Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
    }

    public void showNetworkErrorToast() {
        showToast(R.string.network_error, Toast.LENGTH_SHORT);
    }

    public void showToast(int resId, int duration) {
        showToast(getString(resId), duration);
    }

    public void showToast(String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(this, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public void processCommonError(VolleyError error) {
        if (error == null) {
            showNetworkErrorToast();
            return;
        }
        if (error.networkResponse != null) {
            try {
                String data = new String(error.networkResponse.data, RequestManager.encoding);
                Log.d("请求错误，返回的数据 " + data);
                JSONObject response = new JSONObject(data);
                String return_message = response.optString(Config.key_message, "");
                if (!TextUtils.isEmpty(return_message)) {
                    showToast(return_message, Toast.LENGTH_SHORT);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        showNetworkErrorToast();
    }

    public String getMd5Password(String password) {
        return Md5Util.encrypt(password, Config.salt);
    }
}
