package com.automotive.bautomotive.signIn;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.automotive.bautomotive.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private EditText etMobileNumber;
    private Button btnProceed;
    private ImageView ivBackLogin;

    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        events();
//        printHashKey();
    }

    private void init() {
        etMobileNumber = (EditText) findViewById(R.id.et_mobileNumber);
        btnProceed = (Button) findViewById(R.id.btn_proceed);
        ivBackLogin = (ImageView) findViewById(R.id.iv_backLogin);


    }

    private void events() {
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNumber = etMobileNumber.getText().toString().trim();

                if (verifyMobile()) {
                    Intent intent = new Intent(LoginActivity.this, LoginVerifyPhoneActivity.class);
                    intent.putExtra("mobile", mobileNumber);
                    startActivity(intent);
                }
            }
        });

        ivBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, LoginVerifyPhoneActivity.class);
//                intent.putExtra("mobile", mobileNumber);
//                startActivity(intent);
                finish();
            }
        });
    }

    private boolean verifyMobile() {
        if (mobileNumber.isEmpty() || mobileNumber.length() < 10) {
            etMobileNumber.setError(getString(R.string.enter_valid_mobile_number));
            etMobileNumber.requestFocus();
            return false;
        }

        return true;
    }

//    public void printHashKey() {
//        PackageInfo info;
//        try {
//            info = getPackageManager().getPackageInfo("com.sogo.sogosurvey", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                //String something = new String(Base64.encodeBytes(md.digest()));
//                Log.e("hash key", something);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("exception", e.toString());
//        }
//    }
}
