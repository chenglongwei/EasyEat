package com.easyeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easyeat.adapter.RestaurantAdapter;
import com.easyeat.bean.Restaurant;
import com.easyeat.http.EasyEatJsonArrayRequest;
import com.easyeat.http.EasyEatRequestQueue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class RestaurantsFragment extends Fragment implements AdapterView.OnItemClickListener{
    private SwipeRefreshLayout swipeContainer;
    private ListView lv_restaurants;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurants;

    public RestaurantsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurants, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_restaurants = (ListView) view.findViewById(R.id.lv_restaurants);
        adapter = new RestaurantAdapter(getContext(), restaurants);
        lv_restaurants.setAdapter(adapter);
        lv_restaurants.setOnItemClickListener(this);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                backgroundLoadRestaurants();
            }
        });

        backgroundLoadRestaurants();
    }

    private void backgroundLoadRestaurants() {
        EasyEatJsonArrayRequest restaurantRequest = new EasyEatJsonArrayRequest(
                Request.Method.GET, Config.HTTP_GET_RESTAURANT, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Config.TAG, response.toString());
                        restaurants = new Gson().fromJson(response.toString(), new TypeToken<List<Restaurant>>() {
                        }.getType());

                        adapter.setRestaurants(restaurants);
                        swipeContainer.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Config.TAG, error.toString());
                swipeContainer.setRefreshing(false);
            }
        });

        Map<String, String> params = new HashMap<>();
        params.put(Config.key_latitude, String.valueOf(Config.latitude));
        params.put(Config.key_longtitude, String.valueOf(Config.longtitude));
        restaurantRequest.setParams(params);

        EasyEatRequestQueue.getInstance(getContext()).getRequestQueue().add(restaurantRequest);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Restaurant restaurant = (Restaurant) parent.getAdapter().getItem(position);
        if (restaurant != null) {
            gotoRestaurantIntroActivity(restaurant);
        }
    }

    private void gotoRestaurantIntroActivity(Restaurant restaurant) {
        Intent intent = new Intent(getContext(), RestaurantIntroActivity.class);
        intent.putExtra(Config.key_restaurant, restaurant);
        getContext().startActivity(intent);
    }
}