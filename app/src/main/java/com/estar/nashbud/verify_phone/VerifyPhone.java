package com.estar.nashbud.verify_phone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.estar.nashbud.R;
import com.estar.nashbud.settings.TermandconditionsActivity;
import com.estar.nashbud.utils.SharedPreference;
import com.firebase.client.Firebase;
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
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity implements View.OnClickListener{

    EditText editTextMobileNumber;
    CountryCodePicker ccp;
    Button buttonContinue;
    RequestQueue requestQueue;
    String cCode, otp, resend_otp_existing_users, sMobileNo;
    SharedPreference sharedPreferences;
    String user, pass;
    Context context;
    private ProgressDialog mProgress, dialog;
    private String sMobile,stMobile;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    TextView Prefix_Number;
    ArrayList<TextView> CodeItem=new ArrayList<>();
    private static final String TAG = "PhoneAuthActivity";
    String CCP_Get_Country_Code;
    LinearLayout term_and_condition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_phone_activity);

        context = VerifyPhone.this;
        editTextMobileNumber = findViewById(R.id.editTextMobileNumber);
        Prefix_Number=(TextView)findViewById(R.id.prefix_number);
        ccp = findViewById(R.id.ccp);
        buttonContinue = findViewById(R.id.buttonContinue);
        term_and_condition = findViewById(R.id.term_and_condition);
        ccp.registerCarrierNumberEditText(editTextMobileNumber);
        Prefix_Number.setText(ccp.getDefaultCountryCodeWithPlus());
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Log.e("GetCountryCode",ccp.getSelectedCountryCode());
                Prefix_Number.setText(ccp.getSelectedCountryCodeWithPlus());
            }
        });

        sMobile = ccp.getDefaultCountryCodeWithPlus() + stMobile;

        term_and_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TermandconditionsActivity.class));
            }
        });
        buttonContinue.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonContinue:
                if (isTest()){
                    Intent  i = new Intent(context,VerifyOtp.class);
                    i.putExtra("mobile_no",ccp.getDefaultCountryCodeWithPlus() + stMobile);
                    startActivity(i);
                    break;
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextMobileNumber.setText("");
    }

    private boolean isTest() {
        stMobile = editTextMobileNumber.getText().toString().trim();
        if (stMobile.length() < 10){
            Toast.makeText(context,"Please Enter Valid Mobile Number",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
