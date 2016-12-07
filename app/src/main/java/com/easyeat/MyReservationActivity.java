package com.easyeat;

import android.os.Bundle;
import android.widget.ListView;

import com.easyeat.adapter.ReservationAdapter;
import com.easyeat.bean.ReservationInfo;

public class MyReservationActivity extends BaseActivity {
    private ReservationInfo[] reservationInfos;
    private ListView lv_reservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);
        reservationInfos = (ReservationInfo[]) getIntent().getExtras().getSerializable(Config.key_reservations);

        lv_reservations = (ListView) findViewById(R.id.lv_reservations);
        lv_reservations.setAdapter(new ReservationAdapter(this, reservationInfos));
    }
}
