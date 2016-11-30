package com.easyeat.http;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.easyeat.Config;
import com.easyeat.EasyEatApplication;
import com.easyeat.bean.User;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class EasyEatJsonArrayRequest extends JsonArrayRequest {
    private String url;
    private Map<String, String> params;

    public EasyEatJsonArrayRequest(int method, String url, JSONArray jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.url = super.getUrl();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = new HashMap<>();
        map.putAll(super.getHeaders());
        User user = EasyEatApplication.getCurrentUser();
        long userId = (user == null) ? -1 : user.userId;
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
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Map<String, String> getParams() {
        return params;
    }
}
