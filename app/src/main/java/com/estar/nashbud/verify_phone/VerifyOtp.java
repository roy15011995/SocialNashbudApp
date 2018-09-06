package com.estar.nashbud.verify_phone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.nashbud.R;
import com.estar.nashbud.splash.Splash;
import com.estar.nashbud.upload_photo.UploadPhoto;
import com.estar.nashbud.utils.SharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jkb.vcedittext.VerificationCodeEditText;

import java.util.concurrent.TimeUnit;

/**
 * Created by User on 02-12-2017.
 */

public class VerifyOtp extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId, sMobileNo;
    Button btn_approve;
    TextView textViewResend,tv_mobileNumber;
    VerificationCodeEditText vertext_otp_number;
    Bundle extra;
    Context context;
    SharedPreference sharedPreference;
    TextView _tv;
    ImageView clock_image;

    private static final String TAG = "PhoneAuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_test);
        context = VerifyOtp.this;

        extra = getIntent().getExtras();
        if (extra != null) {
            sMobileNo = extra.getString("mobile_no");
            Log.e("mobile no. getting","" + sMobileNo);
        }

        vertext_otp_number = findViewById(R.id.otp_number);
        btn_approve = findViewById(R.id.btn_approve);
        textViewResend = findViewById(R.id.textViewResend);
        tv_mobileNumber = findViewById(R.id.tv_mobile_number);
        tv_mobileNumber.setText(sMobileNo);
        _tv = (TextView) findViewById( R.id.textView1 );
        clock_image = (ImageView)findViewById(R.id.clock_image);


        // Counter Start
        new CountDownTimer(60000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                _tv.setTextColor(getResources().getColor(R.color.black_de));
                _tv.setText("Resend Code "+String.format("%d0 : %d ",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))).replaceAll("00 :", "")  + "");
                _tv.setVisibility(View.VISIBLE);
                clock_image.setVisibility(View.GONE);
                textViewResend.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                _tv.setVisibility(View.GONE);
                 clock_image.setVisibility(View.GONE);
                textViewResend.setVisibility(View.VISIBLE);
            }
        }.start();



        tv_mobileNumber.setOnClickListener(this);
        textViewResend.setOnClickListener(this);
        btn_approve.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, "Invalid phone number.", Toast.LENGTH_SHORT).show();
                    //editTextMobileNumber.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_mobileNumber.performClick();
            }
        }, 1000);
        vertext_otp_number.setHint("******");

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            Intent i = new Intent(context,UploadPhoto.class);
                            i.putExtra("intent_mobile",sMobileNo);
                            startActivity(i);
                            sharedPreference = new SharedPreference();
                            sharedPreference.savePhNo(context,sMobileNo);
                            finish();
                        } else {
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //mVerificationField.setError("Invalid code.");
                                Toast.makeText(context, "Invalid code.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }



    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        Log.e("auth provider phone no","" + phoneNumber);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = sMobileNo;

        if (TextUtils.isEmpty(phoneNumber)) {
            Log.e("empty phone no","" + phoneNumber);
            //editTextMobileNumber.setError("Invalid phone number.");
            Toast.makeText(context, "Invalid phone number entered.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*@Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(context, VerifyOtp.class));
            finish();
        }
    }*/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_mobile_number:
            if (!validatePhoneNumber()) {
                return;
            }
            startPhoneNumberVerification(sMobileNo);
            //Log.e("phone no","" + sMobileNo);
            break;
            case R.id.btn_approve:
                String code = vertext_otp_number.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    //mVerificationField.setError("Cannot be empty.");
                    Toast.makeText(context, "Cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.textViewResend:
                resendVerificationCode(sMobileNo, mResendToken);

                new CountDownTimer(60000,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        _tv.setTextColor(getResources().getColor(R.color.black_de));
                        _tv.setText("Resend code "+String.format("%d0 : %d",
                                TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))).replaceAll("00 :", "") + "");
                        _tv.setVisibility(View.VISIBLE);
                        textViewResend.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFinish() {
                        _tv.setVisibility(View.GONE);
                        clock_image.setVisibility(View.GONE);
                        textViewResend.setVisibility(View.VISIBLE);
                    }
                }.start();

                break;
        }
    }



}
