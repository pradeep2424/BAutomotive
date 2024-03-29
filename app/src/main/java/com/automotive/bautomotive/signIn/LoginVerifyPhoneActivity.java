package com.automotive.bautomotive.signIn;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.automotive.bautomotive.R;
import com.automotive.bautomotive.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class LoginVerifyPhoneActivity extends AppCompatActivity {

    private OtpTextView otpView;
    //    private Button btnGetStarted;
    private ImageView ivBackVerifyOTP;
    private TextView tvTitleText;
    private TextView tvTimerOTP;
    private TextView tvResendOTP;

    private String mVerificationId;
    private String mobileNumber;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verify_phone);

        mAuth = FirebaseAuth.getInstance();

        init();
        events();
    }

    private void init() {
        tvTitleText = (TextView) findViewById(R.id.tv_otpText);
        tvTimerOTP = (TextView) findViewById(R.id.tv_otpTimer);
        tvResendOTP = (TextView) findViewById(R.id.tv_otpResend);
        otpView = (OtpTextView) findViewById(R.id.otp_view);
//        btnGetStarted = (Button) findViewById(R.id.btn_getStartedNow);
        ivBackVerifyOTP = (ImageView) findViewById(R.id.iv_backVerifyOTP);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mobileNumber = extras.getString("mobile");
            sendVerificationCode(mobileNumber);
            startOTPTimer();
        }

        String titleText = getResources().getString(R.string.login_verify_otp_text)
                .concat(" ").concat(mobileNumber);
        tvTitleText.setText(titleText);
    }

    private void events() {
        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpView.setEnabled(false);
                sendVerificationCode(mobileNumber);
                startOTPTimer();
            }
        });

        otpView.setOtpListener(new OTPListener() {
            @Override
            public void onOTPComplete(String otp) {
                if (otp != null) {
                    verifyVerificationCode(otp);
                }
            }
        });

//        btnGetStarted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String code = otpView.
//                verifyVerificationCode(code);
//            }
//        });

        ivBackVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otpView.setOTP(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginVerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginVerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(LoginVerifyPhoneActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Toast.makeText(LoginVerifyPhoneActivity.this, message, Toast.LENGTH_SHORT).show();

//                            Snackbar snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG);
//                            snackbar.setAction("Dismiss", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                            snackbar.show();
                        }
                    }
                });
    }

    private void startOTPTimer() {
        showTimerCount();

        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                String strTimeText = getString(R.string.otp_wait_for);
                tvTimerOTP.setText(strTimeText + " (00:" + millisUntilFinished / 1000 + ")");
            }

            public void onFinish() {
//                mTextField.setText("done!");
                discardTimerCount();
            }

        }.start();
    }

    private void showTimerCount()
    {
        otpView.setEnabled(false);
        tvTimerOTP.setVisibility(View.VISIBLE);
        tvResendOTP.setVisibility(View.GONE);
    }

    private void discardTimerCount()
    {
        otpView.setEnabled(true);
        tvTimerOTP.setVisibility(View.GONE);
        tvResendOTP.setVisibility(View.VISIBLE);
    }
}
