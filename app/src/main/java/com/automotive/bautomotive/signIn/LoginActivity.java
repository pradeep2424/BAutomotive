package com.automotive.bautomotive.signIn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.automotive.bautomotive.R;

public class LoginActivity extends AppCompatActivity {

    EditText etMobileNumber;
    Button btnProceed;
    ImageView ivBack;

    private View viewMobileNumber;
    private View viewOTP;

    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        viewMobileNumber = getLayoutInflater().inflate(R.layout.activity_login, null);
//        viewOTP = getLayoutInflater().inflate(R.layout.activity_login_verify_number, null);

        setContentView(R.layout.activity_login);

        init();
        events();
    }

    private void init()
    {
        etMobileNumber = (EditText) findViewById(R.id.et_mobileNumber);
        btnProceed = (Button) findViewById(R.id.btn_proceed);
        ivBack  = (ImageView) findViewById(R.id.iv_back);
    }

    private void events()
    {
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNumber = etMobileNumber.getText().toString().trim();
                setContentView(R.layout.activity_login_verify_number);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_login);
            }
        });
    }
}
