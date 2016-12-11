package com.easyeat;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.easyeat.bean.Payment;
import com.easyeat.bean.User;
import com.easyeat.http.BaseResponseListener;
import com.easyeat.http.RequestManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddBankCardActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_card_number;
    private NumberPicker np_month;
    private NumberPicker np_year;
    private EditText et_ccv;

    private EditText et_name;
    private EditText et_address;
    private EditText et_zipcode;
    private Button bt_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card);
        initView();
    }

    private void initView() {
        et_card_number = (EditText) findViewById(R.id.et_card_number);
        np_month = (NumberPicker) findViewById(R.id.np_month);
        np_year = (NumberPicker) findViewById(R.id.np_year);
        et_ccv = (EditText) findViewById(R.id.et_ccv);

        et_name = (EditText) findViewById(R.id.et_name);
        et_address = (EditText) findViewById(R.id.et_address);
        et_zipcode = (EditText) findViewById(R.id.et_zipcode);

        bt_save = (Button) findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);
        initData();
    }

    private void initData() {
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug",
                "Sept", "Oct", "Nov", "Dec"};
        np_month.setMinValue(0);
        np_month.setMaxValue(11);
        np_month.setDisplayedValues(months);
        np_month.setWrapSelectorWheel(true);

        // setup date
        int maxYear = 10;
        String[] years = new String[maxYear];
        for (int i = 0; i < maxYear; i++) {
            years[i] = String.valueOf(2017 + i);
        }
        np_year.setMinValue(0);
        np_year.setMaxValue(years.length - 1);
        np_year.setDisplayedValues(years);
        np_year.setWrapSelectorWheel(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_save:
                attemptSave();
                break;
        }
    }

    private void attemptSave() {
        // Reset errors.
        et_card_number.setError(null);
        et_ccv.setError(null);

        // Store values at the time of the login attempt.
        String number = et_card_number.getText().toString();
        String ccv = et_ccv.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid CCV.
        if (TextUtils.isEmpty(ccv)) {
            et_ccv.setError(getString(R.string.error_field_required));
            focusView = et_ccv;
            cancel = true;
        }

        // Check for a valid card number, if the user entered one.
        if (TextUtils.isEmpty(number)) {
            et_card_number.setError(getString(R.string.error_card_number));
            focusView = et_card_number;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            backgroundSave();
        }
    }

    private void backgroundSave() {
        Payment payment = new Payment();
        payment.cardNumber = et_card_number.getText().toString();
        payment.expiredMonth = String.valueOf(np_month.getValue() + 1);
        payment.expiredYear = String.valueOf(np_year.getValue() + 2017);
        payment.ccv = et_ccv.getText().toString();

        payment.cardHolder = et_name.getText().toString();
        payment.address = et_address.getText().toString();
        payment.zipcode = et_zipcode.getText().toString();

        JSONObject body = new JSONObject();
        try {
            body = new JSONObject(new Gson().toJson(payment));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProgressDialog dialog = ProgressDialog.show(this, "Adding your card...", "Please wait ...");
        RequestManager.backgroundRequest(Request.Method.POST, Config.HTTP_PAYMENT, body, null,
                new BaseResponseListener(this, dialog) {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        JSONArray dataJson = response.optJSONArray(Config.key_data);
                        Payment[] payments = new Gson().fromJson(dataJson.toString(), Payment[].class);

                        User user = EasyEatApplication.getCurrentUser();
                        user.paymentInfo = payments;
                        EasyEatApplication.setCurrentUser(user);

                        showToast("Add card successfully!", Toast.LENGTH_SHORT);
                        finish();
                    }
                });
    }
}
