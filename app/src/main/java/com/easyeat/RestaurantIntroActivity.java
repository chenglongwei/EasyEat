package com.easyeat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.easyeat.bean.Restaurant;
import com.easyeat.http.BaseResponseListener;
import com.easyeat.http.RequestManager;
import com.easyeat.ui.ForwardLayout;

import org.json.JSONObject;

/**
 * Created by chenglongwei on 11/23/16.
 */

public class RestaurantIntroActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_name;
    private TextView tv_address;
    private TextView tv_description;
    private TextView tv_distance;
    private TextView tv_open_hours;
    private ImageView iv_favorite;

    // actions
    private RelativeLayout rl_direction;
    private RelativeLayout rl_phone;

    private RelativeLayout rl_reservation;
    private RelativeLayout rl_take_out;
    private RelativeLayout rl_review_menu;

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
        iv_favorite = (ImageView) findViewById(R.id.iv_favorite);

        rl_direction = (RelativeLayout) findViewById(R.id.rl_direction);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);

        rl_reservation = (RelativeLayout) findViewById(R.id.rl_reservation);
        rl_take_out = (RelativeLayout) findViewById(R.id.rl_take_out);
        rl_review_menu = (RelativeLayout) findViewById(R.id.rl_review_menu);

        initData();
    }

    private void initData() {
        tv_name.setText(restaurant.name);
        tv_address.setText(restaurant.address);
        tv_description.setText(restaurant.description);
        tv_distance.setText(restaurant.distance);
        tv_open_hours.setText("Open hours: " + restaurant.opentime);

        iv_favorite.setImageResource(restaurant.isfavorite ? R.drawable.ic_favor :
                R.drawable.ic_not_favor);
        iv_favorite.setOnClickListener(this);

        ForwardLayout.top(rl_direction, getString(R.string.direction));
        rl_direction.setOnClickListener(this);
        ForwardLayout phone = ForwardLayout.bottom(rl_phone, getString(R.string.phone));
        phone.setValue(restaurant.phonenumber);
        rl_phone.setOnClickListener(this);

        ForwardLayout.top(rl_reservation, getString(R.string.reservation));
        rl_reservation.setOnClickListener(this);
        ForwardLayout.mid(rl_take_out, getString(R.string.take_out));
        rl_take_out.setOnClickListener(this);
        ForwardLayout.bottom(rl_review_menu, getString(R.string.review_menu));
        rl_review_menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_favorite:
                handleFavorite();
                break;
            case R.id.rl_direction:
                openGoogleMaps();
                break;
            case R.id.rl_phone:
                openPhoneDial();
                break;
            case R.id.rl_reservation:
                gotoReservationActivity();
                break;
            case R.id.rl_take_out:
                gotoTakeOutActivity();
                break;
            case R.id.rl_review_menu:
                gotoMenuActivity();
                break;
        }
    }

    private void gotoTakeOutActivity() {
        if (!EasyEatApplication.isLogin()) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            return;
        }

        if (restaurant.slots == null || restaurant.slots.length == 0) {
            showToast("The restaurant has no time slots to order", Toast.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent(this, TakeOutActivity.class);
        intent.putExtra(Config.key_restaurant, restaurant);
        startActivity(intent);
    }

    private void gotoReservationActivity() {
        if (!EasyEatApplication.isLogin()) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            return;
        }

        if (restaurant.slots == null || restaurant.slots.length == 0) {
            showToast("The restaurant has no time slots to reserve", Toast.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent(this, ReservationActivity.class);
        intent.putExtra(Config.key_restaurant, restaurant);
        startActivity(intent);
    }

    private void gotoMenuActivity() {
        if (restaurant.menu == null || restaurant.menu.length == 0) {
            showToast("The restaurant has not uploaded menu yet", Toast.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(Config.key_menu, restaurant.menu);
        startActivity(intent);
    }

    private void handleFavorite() {
        if (!EasyEatApplication.isLogin()) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            return;
        }

        backgroundChangeFavorite();
    }

    private void backgroundChangeFavorite() {
        final String title = restaurant.isfavorite ? "Removing from favorites" : "Adding to favorites";
        int method = restaurant.isfavorite ? Request.Method.DELETE : Request.Method.POST;
        String url = Config.HTTP_GET_FAVORITES_RESTAURANT + "/" + restaurant.id;

        ProgressDialog dialog = ProgressDialog.show(this, title, "Please wait ...");
        RequestManager.backgroundRequest(method, url, null, null,
                new BaseResponseListener(this, dialog) {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        showToast(title + " successfully!", Toast.LENGTH_SHORT);
                        restaurant.isfavorite = !restaurant.isfavorite;
                        iv_favorite.setImageResource(restaurant.isfavorite ? R.drawable.ic_favor :
                                R.drawable.ic_not_favor);
                    }
                });
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
