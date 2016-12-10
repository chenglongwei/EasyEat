package com.easyeat.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.NetworkImageView;
import com.easyeat.BaseActivity;
import com.easyeat.Config;
import com.easyeat.EasyEatApplication;
import com.easyeat.R;
import com.easyeat.bean.Menu;
import com.easyeat.bean.Payment;
import com.easyeat.bean.User;
import com.easyeat.http.BaseResponseListener;
import com.easyeat.http.RequestManager;
import com.google.gson.Gson;

import org.json.JSONObject;


/**
 * Created by chenglongwei on 11/22/16.
 */

public class BankCardAdapter extends BaseAdapter {
    private Payment[] payments;
    private Context context;

    public BankCardAdapter(Context context, Payment[] payments) {
        this.context = context;
        this.payments = payments;
    }

    @Override
    public int getCount() {
        return payments.length;
    }

    @Override
    public Object getItem(int position) {
        if (position >= 0 && position < payments.length) {
            return payments[position];
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        Object obj = getItem(position);
        if (obj != null) {
            return ((Payment) obj).id;
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bank_card, parent, false);
            Holder holder = new Holder();
            holder.tv_card_number = (TextView) convertView.findViewById(R.id.tv_card_number);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_expire_date = (TextView) convertView.findViewById(R.id.tv_expire_date);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            convertView.setTag(holder);
        }

        initData(convertView, position);
        return convertView;
    }

    private void initData(final View convertView, final int position) {
        Holder holder = (Holder) convertView.getTag();
        final Payment item = (Payment) getItem(position);
        holder.tv_card_number.setText("****" + item.cardNumber.substring(item.cardNumber.length() - 4));
        holder.tv_name.setText("Name on Card: " + item.cardHolder);
        holder.tv_expire_date.setText("Expire date: " + item.expiredMonth + "/" + item.expiredYear);

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog dialog = ProgressDialog.show(context, "Deleting your card...", "Please wait ...");
                RequestManager.backgroundRequest(Request.Method.DELETE, Config.HTTP_PAYMENT + "/" + item.id, null, null,
                        new BaseResponseListener((BaseActivity) context, dialog) {
                            @Override
                            public void onSuccessResponse(JSONObject response) {
                                JSONObject dataJson = response.optJSONObject(Config.key_data);
                                Payment[] payments = new Gson().fromJson(dataJson.toString(), Payment[].class);

                                User user = EasyEatApplication.getCurrentUser();
                                user.paymentInfo = payments;
                                EasyEatApplication.setCurrentUser(user);

                                // update listview
                                setPayments(payments);

                                ((BaseActivity) context).showToast("Delete card successfully!", Toast.LENGTH_SHORT);
                            }
                        });
            }
        });
    }

    public void setPayments(Payment[] payments) {
        this.payments = payments;
        notifyDataSetChanged();
    }

    private class Holder {
        TextView tv_card_number;
        TextView tv_name;
        TextView tv_expire_date;
        TextView tv_delete;
    }
}