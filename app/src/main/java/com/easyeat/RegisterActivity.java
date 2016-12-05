package com.easyeat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.easyeat.bean.User;
import com.easyeat.http.BaseResponseListener;
import com.easyeat.http.EasyEatRequest;
import com.easyeat.http.RequestManager;
import com.google.gson.Gson;

import org.json.JSONObject;


public class RegisterActivity extends BaseActivity implements OnClickListener {
    // UI references.
    private EditText et_email;
    private EditText et_password;
    private EditText et_username;
    private EditText et_phone;
    private EditText et_address;
    private Button bt_register;
    private TextView tv_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the login form.
        et_username = (EditText) findViewById(R.id.et_username);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_address = (EditText) findViewById(R.id.et_address);

        bt_register = (Button) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);

        tv_sign_in = (TextView) findViewById(R.id.tv_sign_in);
        tv_sign_in.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_in:
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_register:
                attemptRegister();
                break;
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        // Reset errors.
        et_email.setError(null);
        et_password.setError(null);

        // Store values at the time of the login attempt.
        String name = et_username.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            et_password.setError(getString(R.string.error_invalid_password));
            focusView = et_password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            et_email.setError(getString(R.string.error_field_required));
            focusView = et_email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            et_email.setError(getString(R.string.error_invalid_email));
            focusView = et_email;
            cancel = true;
        }

        // Check for name.
        if (TextUtils.isEmpty(name)) {
            et_username.setError(getString(R.string.error_field_required));
            focusView = et_username;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            backgroundRegister();
        }
    }

    private void backgroundRegister() {
        String name = et_username.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String address = et_address.getText().toString();
        String phone = et_phone.getText().toString();

        JSONObject body = new JSONObject();
        try {
            body.put("username", name);
            body.put("email", email);
            body.put("password", getMd5Password(password));
            body.put("address", address);
            body.put("phone", phone);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProgressDialog dialog = ProgressDialog.show(this, "Registering...", "Please wait ...");
        RequestManager.backgroundRequest(Request.Method.POST, Config.HTTP_POST_REGISTER, body, null,
                new BaseResponseListener(this, dialog) {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        JSONObject dataJson = response.optJSONObject(Config.key_data);
                        JSONObject userJson = dataJson.optJSONObject(Config.key_user);
                        User user = new Gson().fromJson(userJson.toString(), User.class);
                        EasyEatApplication.setCurrentUser(user);

                        String token = dataJson.optString(Config.key_accessToken);
                        EasyEatApplication.setSessionId(token);

                        showToast("Register successfully!", Toast.LENGTH_SHORT);
                        finish();
                    }
                });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}

