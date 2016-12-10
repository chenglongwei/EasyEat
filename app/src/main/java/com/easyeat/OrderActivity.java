package com.easyeat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.easyeat.adapter.BankCardAdapter;
import com.easyeat.adapter.OrderAdapter;
import com.easyeat.bean.Payment;
import com.easyeat.bean.Reservation;
import com.easyeat.bean.ReservationInfo;
import com.easyeat.http.BaseResponseListener;
import com.easyeat.http.RequestManager;
import com.google.gson.Gson;

import org.json.JSONObject;

public class OrderActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ReservationInfo[] reservationInfos;
    private ListView lv_reservations;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        reservationInfos = (ReservationInfo[]) getIntent().getExtras().getSerializable(Config.key_reservations);

        lv_reservations = (ListView) findViewById(R.id.lv_reservations);
        orderAdapter = new OrderAdapter(this, reservationInfos);
        lv_reservations.setAdapter(orderAdapter);
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

    public void checkout(final Reservation reservation) {
        Payment[] payments = EasyEatApplication.getCurrentUser().paymentInfo;
        if (payments == null || payments.length == 0) {
            Intent intent = new Intent(this, AddBankCardActivity.class);
            startActivity(intent);
            return;
        }

        final BankCardAdapter adapter = new BankCardAdapter(this, payments);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Checkout");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                backgroundCheckout(reservation, (Payment) adapter.getItem(item));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void backgroundCheckout(Reservation reservation, Payment payment) {
        JSONObject body = new JSONObject();
        try {
            body.put("paymentId", payment.id);
            body.put("orderId", reservation.id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProgressDialog dialog = ProgressDialog.show(this, "Checking out...", "Please wait ...");
        RequestManager.backgroundRequest(Request.Method.PUT, Config.HTTP_ORDER_CHECKOUT, body, null,
                new BaseResponseListener(this, dialog) {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        JSONObject dataJson = response.optJSONObject(Config.key_data);
                        Reservation update = new Gson().fromJson(dataJson.toString(), Reservation.class);
                        orderAdapter.updateItem(update);

                        showToast("Check out successfully!", Toast.LENGTH_SHORT);
                    }
                });
    }
}
