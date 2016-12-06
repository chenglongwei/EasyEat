package com.easyeat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.easyeat.bean.Restaurant;
import com.easyeat.bean.User;
import com.easyeat.http.BaseResponseListener;
import com.easyeat.http.RequestManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReservationActivity extends BaseActivity implements View.OnClickListener {
    private NumberPicker np_people;
    private NumberPicker np_date;
    private NumberPicker np_slots;
    private CheckBox cb_private;
    private Button bt_reserve;

    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        restaurant = (Restaurant) getIntent().getExtras().getSerializable(Config.key_restaurant);
        initView();
    }

    private void initView() {
        np_people = (NumberPicker) findViewById(R.id.np_people);
        np_date = (NumberPicker) findViewById(R.id.np_date);
        np_slots = (NumberPicker) findViewById(R.id.np_slots);

        cb_private = (CheckBox) findViewById(R.id.cb_private);
        bt_reserve = (Button) findViewById(R.id.bt_reserve);
        bt_reserve.setOnClickListener(this);

        initData();
    }

    private void initData() {
        // setup people
        int maxPeople = 12;
        String[] people = new String[maxPeople];
        for (int i = 0; i < maxPeople; i++) {
            people[i] = i + 1 + " people";
        }
        np_people.setMinValue(0);
        np_people.setMaxValue(people.length - 1);
        np_people.setDisplayedValues(people);
        np_people.setWrapSelectorWheel(true);

        // setup date
        int maxDay = 14;
        String[] date = new String[maxDay];
        for (int i = 0; i < maxDay; i++) {
            date[i] = getCalculatedDate("MM-dd-yyyy", i);
        }
        np_date.setMinValue(0);
        np_date.setMaxValue(date.length - 1);
        np_date.setDisplayedValues(date);
        np_date.setWrapSelectorWheel(true);

        // setup slots
        np_slots.setMinValue(0);
        np_slots.setMaxValue(restaurant.slots.length - 1);
        np_slots.setDisplayedValues(restaurant.slots);
        np_people.setWrapSelectorWheel(true);
    }

    /**
     * Pass your date format and no of days for minus from current
     * If you want to get previous date then pass days with minus sign
     * else you can pass as it is for next date
     *
     * @param dateFormat
     * @param days
     * @return Calculated Date
     */
    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
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

        JSONObject body = new JSONObject();
        try {
            body.put("timeSlot", restaurant.slots[np_slots.getValue()]);
            body.put("isPrivate", cb_private.isChecked());
            body.put("date", getCalculatedDate("MM-dd-yyyy", np_date.getValue()));
            body.put("people", String.valueOf(np_people.getValue() + 1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProgressDialog dialog = ProgressDialog.show(this, "Reserving. ..", "Please wait ...");
        RequestManager.backgroundRequest(Request.Method.POST, Config.HTTP_POST_TABLE_RESERVE + "/" + restaurant.id,
                body, null, new BaseResponseListener(this, dialog) {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        showToast("Reserve successfully!", Toast.LENGTH_SHORT);
                        finish();
                    }
                });
    }
}
