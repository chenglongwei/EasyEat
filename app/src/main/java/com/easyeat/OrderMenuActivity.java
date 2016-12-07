package com.easyeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.easyeat.adapter.OrderMenuAdapter;
import com.easyeat.bean.MenuQuality;

public class OrderMenuActivity extends AppCompatActivity {

    private ListView lv_menu;
    private MenuQuality[] menus;
    private OrderMenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_menu);

        lv_menu = (ListView) findViewById(R.id.lv_menu);
        menus = (MenuQuality[]) getIntent().getExtras().getSerializable(Config.key_menu);
        adapter = new OrderMenuAdapter(this, menus);
        lv_menu.setAdapter(adapter);
    }
}
