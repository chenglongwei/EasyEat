package com.easyeat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easyeat.R;
import com.easyeat.bean.Menu;
import com.easyeat.http.RequestManager;


/**
 * Created by chenglongwei on 11/22/16.
 */

public class TakeOutAdapter extends BaseAdapter {
    private Menu[] menu;
    private Context context;

    public TakeOutAdapter(Context context, Menu[] menu) {
        this.context = context;
        this.menu = menu;
    }

    @Override
    public int getCount() {
        return menu.length;
    }

    @Override
    public Object getItem(int position) {
        if (position >= 0 && position < menu.length) {
            return menu[position];
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        Object obj = getItem(position);
        if (obj != null) {
            return ((Menu) obj).id;
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_take_out, parent, false);
            Holder holder = new Holder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.np_count = (NumberPicker) convertView.findViewById(R.id.np_count);
            holder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.networkImageView);
            convertView.setTag(holder);
        }

        initData(convertView, position);
        return convertView;
    }

    private void initData(final View convertView, final int position) {
//         String demoUrl = "https://static1.squarespace.com/static/56f43a462eeb819927f7039d/56f5e1e11d07c03c85cd8d1c/5713e8e122482eca2d3bcdf6/1460922599327/dining+hall+3.jpg";
        Holder holder = (Holder) convertView.getTag();
        Menu item = (Menu) getItem(position);
        holder.tv_name.setText(item.name);
        holder.tv_description.setText(item.description);
        holder.tv_price.setText("$" + String.valueOf(item.price));

        holder.np_count.setMinValue(0);
        holder.np_count.setMaxValue(3);
        holder.np_count.setWrapSelectorWheel(true);
        holder.np_count.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                ((Menu) getItem(position)).count = newVal;
            }
        });

        holder.networkImageView.setImageUrl(item.image, RequestManager.getImageLoader());
    }

    public Menu[] getMenu() {
        return menu;
    }

    private class Holder {
        TextView tv_name;
        TextView tv_description;
        TextView tv_price;
        NumberPicker np_count;
        NetworkImageView networkImageView;
    }
}