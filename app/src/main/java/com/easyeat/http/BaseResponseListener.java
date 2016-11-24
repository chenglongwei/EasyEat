package com.easyeat.http;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.easyeat.BaseActivity;
import com.easyeat.Config;

import org.json.JSONObject;

/**
 * Created by chenglongwei on 11/23/16.
 */

public abstract class BaseResponseListener implements EasyEatResponseListener<JSONObject> {
    private BaseActivity activity;
    private Dialog dialog;

    public BaseResponseListener(BaseActivity activity, Dialog dialog) {
        this.activity = activity;
        this.dialog = dialog;
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d(Config.TAG, "request info" + response.toString());
        if (dialog != null) {
            dialog.dismiss();
        }

        String status = response.optString(Config.key_status, "");
        if (Config.OK.equalsIgnoreCase(status)) {
            onSuccessResponse(response);
            return;
        }
        if (response.isNull(Config.key_message)) {
            return;
        }

        String message = response.optString(Config.key_message);
        if (!TextUtils.isEmpty(message)) {
            activity.showToast(message, Toast.LENGTH_SHORT);
        }
    }

    abstract public void onSuccessResponse(JSONObject response);

    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (activity != null) {
            activity.processCommonError(error);
        }
    }
}
