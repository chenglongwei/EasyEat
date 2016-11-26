package com.easyeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.easyeat.ui.ForwardLayout;

/**
 * Created by chenglongwei on 11/22/16.
 */

public class MyFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout rl_my_account;

    private RelativeLayout rl_food_order;
    private RelativeLayout rl_reservation;
    private RelativeLayout rl_about_us;

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
        rl_food_order = (RelativeLayout) view.findViewById(R.id.rl_food_order);
        rl_reservation = (RelativeLayout) view.findViewById(R.id.rl_reservation);
        rl_about_us = (RelativeLayout) view.findViewById(R.id.rl_about_us);
        initView();
    }

    private void initView() {
        //My Account
        rl_my_account.setOnClickListener(this);
        ForwardLayout layout = ForwardLayout.single(rl_my_account, getString(R.string.my_account));
        layout.setValue(getAccountStatus());

        // Food Order
        rl_food_order.setOnClickListener(this);
        ForwardLayout.top(rl_food_order, getString(R.string.food_order));
        // Reservation
        rl_reservation.setOnClickListener(this);
        ForwardLayout.mid(rl_reservation, getString(R.string.reservation));
        // About us
        rl_about_us.setOnClickListener(this);
        ForwardLayout.bottom(rl_about_us, getString(R.string.about_us));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_account:
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.rl_food_order:
                break;
            case R.id.rl_reservation:
                break;
            case R.id.rl_about_us:
                break;
        }
    }

    public String getAccountStatus() {
        if (!EasyEatApplication.isLogin()) {
            return "Not login";
        }

        return EasyEatApplication.getCurrentUser().user_name;
    }
}