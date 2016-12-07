package com.easyeat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.easyeat.adapter.OrderAdapter;
import com.easyeat.bean.ReservationInfo;
import com.easyeat.util.Log;

public class OrderActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ReservationInfo[] reservationInfos;
    private ListView lv_reservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        reservationInfos = (ReservationInfo[]) getIntent().getExtras().getSerializable(Config.key_reservations);

        lv_reservations = (ListView) findViewById(R.id.lv_reservations);
        lv_reservations.setAdapter(new OrderAdapter(this, reservationInfos));
        lv_reservations.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ReservationInfo reservationInfo = (ReservationInfo) parent.getAdapter().getItem(position);
        if (reservationInfo.menus == null || reservationInfo.menus.length == 0) {
            showToast("No order menus found", Toast.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent(this, OrderMenuActivity.class);
        intent.putExtra(Config.key_menu, reservationInfo.menus);
        startActivity(intent);
    }
}
