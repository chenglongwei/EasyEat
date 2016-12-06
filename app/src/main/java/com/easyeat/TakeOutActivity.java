package com.easyeat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.easyeat.adapter.TakeOutAdapter;
import com.easyeat.bean.Menu;
import com.easyeat.bean.Restaurant;
import com.easyeat.http.BaseResponseListener;
import com.easyeat.http.RequestManager;
import com.easyeat.util.Log;
import com.easyeat.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

public class TakeOutActivity extends BaseActivity implements View.OnClickListener {
    private NumberPicker np_date;
    private NumberPicker np_slots;
    private Button bt_reserve;
    private ListView lv_menu;

    private Restaurant restaurant;
    private TakeOutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_out);

        restaurant = (Restaurant) getIntent().getExtras().getSerializable(Config.key_restaurant);
        initView();
    }

    private void initView() {
        np_date = (NumberPicker) findViewById(R.id.np_date);
        np_slots = (NumberPicker) findViewById(R.id.np_slots);

        bt_reserve = (Button) findViewById(R.id.bt_reserve);
        bt_reserve.setOnClickListener(this);

        lv_menu = (ListView) findViewById(R.id.lv_menu);

        initData();
    }

    private void initData() {
        // setup date
        int maxDay = 14;
        String[] date = new String[maxDay];
        for (int i = 0; i < maxDay; i++) {
            date[i] = Util.getCalculatedDate("MM-dd-yyyy", i);
        }
        np_date.setMinValue(0);
        np_date.setMaxValue(date.length - 1);
        np_date.setDisplayedValues(date);
        np_date.setWrapSelectorWheel(true);

        // setup slots
        np_slots.setMinValue(0);
        np_slots.setMaxValue(restaurant.slots.length - 1);
        np_slots.setDisplayedValues(restaurant.slots);
        np_slots.setWrapSelectorWheel(true);

        // init listview
        adapter = new TakeOutAdapter(this, restaurant.menu);
        lv_menu.setAdapter(adapter);

        ViewGroup.LayoutParams params = lv_menu.getLayoutParams();
        params.height = restaurant.menu.length *
                getResources().getDimensionPixelSize(R.dimen.item_restaurant_height);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_reserve:
                backgroundReserve();
                break;
        }
    }

    private void backgroundReserve() {
        JSONArray menuArray = new JSONArray();
        for (Menu menu : adapter.getMenu()) {
            if (menu.count > 0) {
                try {
                    JSONObject menuJson = new JSONObject();
                    menuJson.put("menuId", menu.id);
                    menuJson.put("quatity", menu.count);
                    menuArray.put(menuJson);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (menuArray.length() == 0) {
            showToast("Please select order items!", Toast.LENGTH_SHORT);
            return;
        }

        JSONObject body = new JSONObject();
        try {
            body.put("timeSlot", restaurant.slots[np_slots.getValue()]);
            body.put("isPrivate", true);
            body.put("takeOut", true);
            body.put("date", Util.getCalculatedDate("MM-dd-yyyy", np_date.getValue()));
            body.put("menus", menuArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(body.toString());

        ProgressDialog dialog = ProgressDialog.show(this, "Ordering. ..", "Please wait ...");
        RequestManager.backgroundRequest(Request.Method.POST, Config.HTTP_POST_TABLE_RESERVE + "/" + restaurant.id,
                body, null, new BaseResponseListener(this, dialog) {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        showToast("Order successfully!", Toast.LENGTH_SHORT);
                        finish();
                    }
                });
    }
}
