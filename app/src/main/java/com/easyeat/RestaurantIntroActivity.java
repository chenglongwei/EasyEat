package com.easyeat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easyeat.bean.Restaurant;
import com.easyeat.ui.ForwardLayout;

/**
 * Created by chenglongwei on 11/23/16.
 */

public class RestaurantIntroActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_name;
    private TextView tv_address;
    private TextView tv_description;
    private TextView tv_distance;
    private TextView tv_open_hours;

    // actions
    private RelativeLayout rl_direction;
    private RelativeLayout rl_phone;

    private RelativeLayout rl_reservation;
    private RelativeLayout rl_take_out;
    private RelativeLayout rl_find_table;

    private Restaurant restaurant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_intro);

        restaurant = (Restaurant) getIntent().getExtras().getSerializable(Config.key_restaurant);
        setTitle(restaurant.name);

        initView();
    }

    private void initView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_open_hours = (TextView) findViewById(R.id.tv_open_hours);

        rl_direction = (RelativeLayout) findViewById(R.id.rl_direction);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);

        rl_reservation = (RelativeLayout) findViewById(R.id.rl_reservation);
        rl_take_out = (RelativeLayout) findViewById(R.id.rl_take_out);
        rl_find_table = (RelativeLayout) findViewById(R.id.rl_find_table);

        initData();
    }

    private void initData() {
        tv_name.setText(restaurant.name);
        tv_address.setText(restaurant.address);
        tv_description.setText(restaurant.description);
        tv_distance.setText(restaurant.distance);
        tv_open_hours.setText("Open hours: " + restaurant.opentime);

        ForwardLayout.top(rl_direction, getString(R.string.direction));
        rl_direction.setOnClickListener(this);
        ForwardLayout phone = ForwardLayout.bottom(rl_phone, getString(R.string.phone));
        phone.setValue(restaurant.phonenumber);
        rl_phone.setOnClickListener(this);

        ForwardLayout.top(rl_reservation, getString(R.string.reservation));
        rl_reservation.setOnClickListener(this);
        ForwardLayout.mid(rl_take_out, getString(R.string.take_out));
        rl_take_out.setOnClickListener(this);
        ForwardLayout.bottom(rl_find_table, getString(R.string.find_table));
        rl_find_table.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_direction:
                openGoogleMaps();
                break;
            case R.id.rl_phone:
                openPhoneDial();
                break;
            case R.id.rl_reservation:
                break;
            case R.id.rl_take_out:
                break;
            case R.id.rl_find_table:
                break;
        }
    }

    private void openGoogleMaps() {
        String uri = "geo:0,0?q=" + restaurant.address;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    private void openPhoneDial() {
        // set the data
        String uri = "tel:" + restaurant.phonenumber;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        startActivity(intent);
    }
}
