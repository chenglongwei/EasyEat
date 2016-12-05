package com.easyeat;

import android.os.Bundle;
import android.widget.ListView;

import com.easyeat.adapter.MenuAdapter;
import com.easyeat.bean.Menu;

public class MenuActivity extends BaseActivity {
    private ListView lv_menu;
    private Menu[] menu;
    private MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        lv_menu = (ListView) findViewById(R.id.lv_menu);
        menu = (Menu[]) getIntent().getExtras().getSerializable(Config.key_menu);
        adapter = new MenuAdapter(this, menu);
        lv_menu.setAdapter(adapter);
    }
}
