package com.easyeat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.easyeat.bean.ReservationInfo;
import com.easyeat.http.BaseResponseListener;
import com.easyeat.http.RequestManager;
import com.easyeat.ui.ForwardLayout;
import com.easyeat.util.Log;
import com.easyeat.util.Util;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class MyFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout rl_my_account;
    private RelativeLayout rl_my_payment;

    private RelativeLayout rl_food_order;
    private RelativeLayout rl_reservation;
    private RelativeLayout rl_about_us;

    private ForwardLayout fl_account;

    public MyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rl_my_account = (RelativeLayout) view.findViewById(R.id.rl_my_account);
        rl_my_payment = (RelativeLayout) view.findViewById(R.id.rl_my_payment);
        rl_food_order = (RelativeLayout) view.findViewById(R.id.rl_food_order);
        rl_reservation = (RelativeLayout) view.findViewById(R.id.rl_reservation);
        rl_about_us = (RelativeLayout) view.findViewById(R.id.rl_about_us);
        initView();
    }

    private void initView() {
        // My Account
        rl_my_account.setOnClickListener(this);
        fl_account = ForwardLayout.top(rl_my_account, getString(R.string.my_account));

        // My Payment
        rl_my_payment.setOnClickListener(this);
        ForwardLayout.bottom(rl_my_payment, getString(R.string.rl_my_payment));

        // Food Order
        rl_food_order.setOnClickListener(this);
        ForwardLayout.top(rl_food_order, getString(R.string.food_order));
        // Reservation
        rl_reservation.setOnClickListener(this);
        ForwardLayout.mid(rl_reservation, getString(R.string.my_reservation));
        // About us
        rl_about_us.setOnClickListener(this);
        ForwardLayout.bottom(rl_about_us, getString(R.string.about_us));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("update account status");
        fl_account.setValue(getAccountStatus());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_account:
                gotoProfileActivity();
                break;
            case R.id.rl_my_payment:
                gotoMyPaymentActivity();
                break;
            case R.id.rl_food_order:
                gotoOrderActivity();
                break;
            case R.id.rl_reservation:
                gotoMyReservationActivity();
                break;
            case R.id.rl_about_us:
                break;
        }
    }

    private void gotoMyPaymentActivity() {
        if (!EasyEatApplication.isLogin()) {
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            startActivity(intent);
            return;
        }

        Intent intent = new Intent(getActivity(), BankCardActivity.class);
        startActivity(intent);
    }

    private void gotoOrderActivity() {
        if (!EasyEatApplication.isLogin()) {
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            startActivity(intent);
            return;
        }

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "Getting your orders...",
                "Please wait ...");
        String start = Util.getCalculatedDate(Util.DATE_FORMAT, 0);
        String end = Util.getCalculatedDate(Util.DATE_FORMAT, 14);
        String url = Config.HTTP_GET_ORDER_TAKEOUT + "?start=" + start + "&end=" + end;
        RequestManager.backgroundRequest(Request.Method.GET, url, null, null,
                new BaseResponseListener((BaseActivity) getActivity(), dialog) {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        JSONArray dataJson = response.optJSONArray(Config.key_data);
                        ReservationInfo[] reservationInfos =
                                new Gson().fromJson(dataJson.toString(), ReservationInfo[].class);

                        Intent intent = new Intent(getActivity(), OrderActivity.class);
                        intent.putExtra(Config.key_reservations, reservationInfos);
                        startActivity(intent);
                    }
                });
    }

    private void gotoMyReservationActivity() {
        if (!EasyEatApplication.isLogin()) {
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            startActivity(intent);
            return;
        }

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "Getting your reservation...",
                "Please wait ...");
        String start = Util.getCalculatedDate(Util.DATE_FORMAT, 0);
        String end = Util.getCalculatedDate(Util.DATE_FORMAT, 14);
        String url = Config.HTTP_GET_TABLE_RESERVE + "?start=" + start + "&end=" + end;
        RequestManager.backgroundRequest(Request.Method.GET, url, null, null,
                new BaseResponseListener((BaseActivity) getActivity(), dialog) {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        JSONArray dataJson = response.optJSONArray(Config.key_data);
                        ReservationInfo[] reservationInfos =
                                new Gson().fromJson(dataJson.toString(), ReservationInfo[].class);

                        Intent intent = new Intent(getActivity(), MyReservationActivity.class);
                        intent.putExtra(Config.key_reservations, reservationInfos);
                        startActivity(intent);
                    }
                });
    }

    private void gotoProfileActivity() {
        Intent intent;
        if (EasyEatApplication.isLogin()) {
            intent = new Intent(getActivity(), ProfileActivity.class);
        } else {
            intent = new Intent(getActivity(), SignInActivity.class);
        }
        getActivity().startActivity(intent);
    }

    public String getAccountStatus() {
        if (!EasyEatApplication.isLogin()) {
            return "Not login";
        }

        return EasyEatApplication.getCurrentUser().username;
    }
}