package com.easyeat.http;

import com.android.volley.VolleyError;

/**
 * Created by chenglongwei on 11/23/16.
 */

public interface EasyEatResponseListener<T> {
    public void onResponse(T response);

    public void onErrorResponse(VolleyError error);
}
