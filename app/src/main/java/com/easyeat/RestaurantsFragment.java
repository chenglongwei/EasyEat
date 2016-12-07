package com.easyeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.easyeat.adapter.RestaurantAdapter;
import com.easyeat.bean.Restaurant;
import com.easyeat.http.BaseResponseListener;
import com.easyeat.http.RequestManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class RestaurantsFragment extends Fragment implements AdapterView.OnItemClickListener {
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
                backgroundLoadRestaurants("");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        backgroundLoadRestaurants("");
    }

    private void backgroundLoadRestaurants(String query) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.key_latitude, String.valueOf(Config.latitude));
        params.put(Config.key_longtitude, String.valueOf(Config.longtitude));
        params.put(Config.key_name, query);

        RequestManager.backgroundRequest(Request.Method.GET, Config.HTTP_GET_RESTAURANT, params,
                new BaseResponseListener((BaseActivity) getActivity(), null) {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        swipeContainer.setRefreshing(false);

                        JSONArray data = response.optJSONArray(Config.key_data);
                        restaurants = new Gson().fromJson(data.toString(), new TypeToken<List<Restaurant>>() {
                        }.getType());

                        adapter.setRestaurants(restaurants);
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        super.onResponse(response);
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        swipeContainer.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Restaurant restaurant = (Restaurant) parent.getAdapter().getItem(position);
        if (restaurant != null) {
            gotoRestaurantIntroActivity(restaurant);
        }
    }

    private void gotoRestaurantIntroActivity(Restaurant restaurant) {
        Intent intent = new Intent(getActivity(), RestaurantIntroActivity.class);
        intent.putExtra(Config.key_restaurant, restaurant);
        getActivity().startActivity(intent);
    }

    public void submitQuery(String query) {
        backgroundLoadRestaurants(query);
    }
}