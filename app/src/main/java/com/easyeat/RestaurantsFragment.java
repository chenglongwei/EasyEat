package com.easyeat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.easyeat.http.EasyEatJsonArrayRequest;
import com.easyeat.http.EasyEatRequestQueue;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class RestaurantsFragment extends Fragment {
    EasyEatJsonArrayRequest restaurantRequest;
    public RestaurantsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurants, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        restaurantRequest = new EasyEatJsonArrayRequest(
                Request.Method.GET, Config.HTTP_GET_RESTAURANT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Config.TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Config.TAG, error.toString());
            }
        });

        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        restaurantRequest.setParams(params);

        EasyEatRequestQueue.getInstance(getContext()).getRequestQueue().add(restaurantRequest);
    }
}