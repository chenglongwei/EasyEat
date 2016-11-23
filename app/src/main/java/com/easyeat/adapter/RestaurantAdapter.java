package com.easyeat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easyeat.R;
import com.easyeat.bean.Restaurant;
import com.easyeat.http.EasyEatRequestQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class RestaurantAdapter extends BaseAdapter {
    private List<Restaurant> restaurants;
    private Context context;

    public RestaurantAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        setRestaurants(restaurants);
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= 0 && position < restaurants.size()) {
            return restaurants.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        Object obj = getItem(position);
        if (obj != null) {
            return ((Restaurant) obj).id;
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
            Holder holder = new Holder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
            holder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.networkImageView);
            convertView.setTag(holder);
        }

        initData(convertView, position);
        return convertView;
    }

    private void initData(final View convertView, final int position) {
        // String demoUrl = "https://static1.squarespace.com/static/56f43a462eeb819927f7039d/56f5e1e11d07c03c85cd8d1c/5713e8e122482eca2d3bcdf6/1460922599327/dining+hall+3.jpg";
        Holder holder = (Holder) convertView.getTag();
        Restaurant item = (Restaurant) getItem(position);
        holder.tv_name.setText(item.name);
        holder.tv_address.setText(item.address);
        holder.tv_description.setText(item.description);
        holder.networkImageView.setImageUrl(item.url, EasyEatRequestQueue.getInstance(context.getApplicationContext()).getImageLoader());
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        if (restaurants != null && !restaurants.isEmpty()) {
            this.restaurants = new ArrayList<>(restaurants);
        } else {
            this.restaurants = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    private class Holder {
        TextView tv_name;
        TextView tv_address;
        TextView tv_description;
        NetworkImageView networkImageView;
    }
}