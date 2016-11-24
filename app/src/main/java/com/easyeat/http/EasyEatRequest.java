package com.easyeat.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.easyeat.Config;
import com.easyeat.EasyEatApplication;
import com.easyeat.bean.User;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class EasyEatRequest extends JsonObjectRequest {
    private String url;
    private Map<String, String> params;
    private final Response.Listener listener;

    public EasyEatRequest(int method, String url,
                          final EasyEatResponseListener<JSONObject> listener) {
        this(method, url, null, listener);
    }

    public EasyEatRequest(int method, String url, JSONObject jsonRequest,
                          final EasyEatResponseListener<JSONObject> listener) {
        this(method, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listener.onResponse(response);
                } catch (Exception e) {
                    if (e.getMessage() != null) {
                        Log.e(Config.TAG, e.getMessage());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    listener.onErrorResponse(error);
                } catch (Exception e) {
                    if (e.getMessage() != null) {
                        Log.e(Config.TAG, e.getMessage());
                    }
                }
            }
        });
        this.url = url;
    }

    public EasyEatRequest(int method, String url, JSONObject jsonRequest,
                          Response.Listener<JSONObject> listener,
                          Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.listener = listener;
        this.url = url;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = new HashMap<>();
        map.putAll(super.getHeaders());
        User user = EasyEatApplication.getCurrentUser();
        long userId = (user == null) ? -1 : user.user_id;
        map.put(Config.key_user_id, String.valueOf(userId));
        map.put(Config.key_session_id, EasyEatApplication.getSessionId());
        return map;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;

        //更新url
        StringBuilder sbURL;
        int idx = url.indexOf("?");
        if (idx > 0) {
            sbURL = new StringBuilder(url.substring(0, idx + 1));
        } else {
            sbURL = new StringBuilder(url).append("?");
        }
        //更新参数
        StringBuilder sbParam = new StringBuilder();
        String preParam = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null) {
                value = "";
            }
            sbParam.append(preParam).append(urlEncode(key)).append("=").append(urlEncode(value));
            preParam = "&";
        }

        //更新完成
        this.url = sbURL.append(sbParam).toString();
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, RequestManager.encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Map<String, String> getParams() {
        return params;
    }
}
