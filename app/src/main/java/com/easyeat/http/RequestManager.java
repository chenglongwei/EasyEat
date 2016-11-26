package com.easyeat.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.easyeat.Config;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class RequestManager {
    public static final String encoding = "UTF-8";
    private static RequestQueue requestQueue;
    private static ImageLoader imageLoader;

    private RequestManager() {
    }

    public static void init(Context context) {
        // getApplicationContext() is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new LruBitmapCache(context));
    }

    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            throw new IllegalStateException("request queue not initialized");
        }
        return requestQueue;
    }

    public static ImageLoader getImageLoader() {
        if (imageLoader == null) {
            throw new IllegalStateException("image loader not initialized");
        }
        return imageLoader;
    }

    public static void backgroundRequest(int method, String url, Map<String, String> params,
                                         EasyEatResponseListener<JSONObject> listener) {
        EasyEatRequest request = new EasyEatRequest(method, url, listener);

        RequestQueue queue = RequestManager.getRequestQueue();
        Log.d(Config.TAG, "cache key: " + request.getCacheKey());
        Log.d(Config.TAG, "request info" + request.getUrl());
        queue.add(request);
    }

    public static void backgroundRequest(int method, String url, JSONObject jsonObject, Map<String, String> params,
                                         EasyEatResponseListener<JSONObject> listener) {
        EasyEatRequest request = new EasyEatRequest(method, url, jsonObject, listener);
        request.setParams(params);

        RequestQueue queue = RequestManager.getRequestQueue();
        Log.d(Config.TAG, "cache key: " + request.getCacheKey());
        Log.d(Config.TAG, "request info" + request.getUrl());
        queue.add(request);
    }
}
