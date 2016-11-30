package com.easyeat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.easyeat.bean.User;
import com.easyeat.http.BaseResponseListener;
import com.easyeat.http.RequestManager;
import com.easyeat.util.SharedPrefsUtil;

import org.json.JSONObject;


public class ProfileActivity extends BaseActivity implements OnClickListener {
    // UI references.
    private EditText et_username;
    private EditText et_email;
    private EditText et_phone;
    private EditText et_address;
    private Button bt_save;
    private TextView tv_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set up the login form.
        et_email = (EditText) findViewById(R.id.et_email);
        et_username = (EditText) findViewById(R.id.et_username);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_address = (EditText) findViewById(R.id.et_address);

        bt_save = (Button) findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);

        tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(this);
        initData();
    }

    private void initData() {
        User user = EasyEatApplication.getCurrentUser();
        et_email.setText(user.email);
        et_email.setEnabled(false);
        et_username.setText(user.username);
        et_username.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_save:
                backgroundSaveProfile();
                break;
            case R.id.tv_logout:
                //清空存储的数据
                SharedPrefsUtil.deleteUser();
                //设置变量为空
                EasyEatApplication.setCurrentUser(null);
                EasyEatApplication.setSessionId(null);
                finish();
                break;
        }
    }

    private void backgroundSaveProfile() {
        ProgressDialog dialog = ProgressDialog.show(this, "Updating profile", "Please wait ...");
        RequestManager.backgroundRequest(Request.Method.POST, Config.HTTP_POST_UPDATE_PROFILE,
                null, null, new BaseResponseListener(this, dialog) {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        showToast(response.optString(Config.key_message), Toast.LENGTH_SHORT);
                    }
                });
    }
}

