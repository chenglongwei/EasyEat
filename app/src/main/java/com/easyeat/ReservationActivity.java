package com.easyeat;

import android.os.Bundle;
import android.widget.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReservationActivity extends BaseActivity {
    private String[] slots;
    private NumberPicker np_people;
    private NumberPicker np_date;
    private NumberPicker np_slots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        slots = (String[]) getIntent().getExtras().getSerializable(Config.key_slots);
        initView();
    }

    private void initView() {
        np_people = (NumberPicker) findViewById(R.id.np_people);
        np_date = (NumberPicker) findViewById(R.id.np_date);
        np_slots = (NumberPicker) findViewById(R.id.np_slots);

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
        np_slots.setMaxValue(slots.length - 1);
        np_slots.setDisplayedValues(slots);
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
}
