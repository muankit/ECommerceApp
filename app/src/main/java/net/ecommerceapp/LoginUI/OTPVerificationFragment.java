package net.ecommerceapp.LoginUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.ecommerceapp.Home.MainActivity;
import net.ecommerceapp.R;
import net.ecommerceapp.Utils.DeviceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OTPVerificationFragment extends Fragment {

    View view;

    EditText otp1,otp2,otp3,otp4,otp5,otp6;
    TextView countDownText , tvChangeNumber, tvResendOtp;
    AppCompatButton btnVerify;
    //countdown timer declaration
    private long seconds, minutes, millisRemaining;
    CountDownTimer mloginCountDownTimer = null;

    private String mloginVerificationId;
    private PhoneAuthProvider.ForceResendingToken mloginResendToken;

    String phoneNumber;

    Map<String, Object> userData;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_o_t_p_verification, container, false);

        db = FirebaseFirestore.getInstance();

        Bundle bundle = getArguments();
        phoneNumber = "+91" + bundle.getString("EnteredMobile");

        otp1 = view.findViewById(R.id.etOTP1); otp1.addTextChangedListener(new GenericTextWatcher(otp1));
        otp2 = view.findViewById(R.id.etOTP2); otp2.addTextChangedListener(new GenericTextWatcher(otp2));
        otp3 = view.findViewById(R.id.etOTP3); otp3.addTextChangedListener(new GenericTextWatcher(otp3));
        otp4 = view.findViewById(R.id.etOTP4); otp4.addTextChangedListener(new GenericTextWatcher(otp4));
        otp5 = view.findViewById(R.id.etOTP5); otp5.addTextChangedListener(new GenericTextWatcher(otp5));
        otp6 = view.findViewById(R.id.etOTP6); otp6.addTextChangedListener(new GenericTextWatcher(otp6));

        countDownText = view.findViewById(R.id.countdown_text);
        tvChangeNumber = view.findViewById(R.id.tv_change_number);
        tvResendOtp = view.findViewById(R.id.tv_resent_otp);
        btnVerify = view.findViewById(R.id.btn_verify);

        sendVerificationOTP();

        tvChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        (Activity) view.getContext(),               // Activity (for callback binding)
                        mCallbacks,         // OnVerificationStateChangedCallbacks
                        mloginResendToken);

            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entered_otp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString()
                        + otp5.getText().toString() + otp6.getText().toString();
                verifyCode(entered_otp);
            }
        });

        return view;
    }

    private void checkUserExistenceAndProceed() {

        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userData = new HashMap<>();
        userData.put("MobileNumber", phoneNumber);
        userData.put("RegisteredWith","Mobile");
        userData.put("DeviceIMEI", DeviceUtils.getDeviceIMEI(view.getContext()));
        userData.put("DeviceName",DeviceUtils.getDeviceName());
        userData.put("DeviceVersion",DeviceUtils.getDeviceVersion());
        userData.put("RootStatus",DeviceUtils.isDeviceRooted());
        userData.put("AccountCreationTime", FieldValue.serverTimestamp());

        db.collection("Users").whereEqualTo("MobileNumber",phoneNumber).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(Objects.requireNonNull(task.getResult()).size() > 0){
                            //User Already Exist

                            Log.d("accountcreation", "onComplete: user exist");

                            db.collection("Users").document(currentUser).update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent mobileSigninIntent = new Intent(view.getContext(), MainActivity.class);
                                    mobileSigninIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mobileSigninIntent);
                                }
                            });

                        }else{
                            //add a new user to Firestore database
                            Log.d("accountcreation", "onComplete: user not exist");

                            db.collection("Users").document(currentUser).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent mobileSigninIntent = new Intent(view.getContext(), MainActivity.class);
                                    mobileSigninIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mobileSigninIntent);
                                }
                            });
                        }
                    }
                });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener((Activity) view.getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            checkUserExistenceAndProceed();

                        } else {
                            Snackbar.make(view,"Kindly check the OTP you have entered",Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void verifyCode(String otp) {
        PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(mloginVerificationId, otp);
        signInWithPhoneAuthCredential(credentials);
    }

    public void startCountDown(){
        mloginCountDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                millisRemaining = millisUntilFinished;

                seconds = (long) (millisUntilFinished / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                countDownText.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));

            }

            @Override
            public void onFinish() {
                //Do what you want
                countDownText.setVisibility(View.INVISIBLE);
                tvResendOtp.setVisibility(View.VISIBLE);
            }
        };
        mloginCountDownTimer.start();
    }

    private void sendVerificationOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                (Activity) view.getContext(),               // Activity (for callback binding)
                mCallbacks);
        startCountDown();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            String receivedCode = credential.getSmsCode();

            if (receivedCode != null) {
                //verifyCode(sendedCode);
                Log.d("sended code", receivedCode);
            }
            else {
                signInWithPhoneAuthCredential(credential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Toast.makeText(view.getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(view.getContext(), "Quota exceeded", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,@NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId, token);

            mloginVerificationId = verificationId;
            mloginResendToken = token;

        }
    };

    public class GenericTextWatcher implements TextWatcher {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }
        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.etOTP1:
                    if (text.length() > 1) {
                        otp1.setText(String.valueOf(text.charAt(0)));
                        otp2.setText(String.valueOf(text.charAt(1)));
                        otp2.requestFocus();
                        otp2.setSelection(otp2.getText().length());
                    }
                    break;
                case R.id.etOTP2:
                    if (text.length() > 1){
                        otp2.setText(String.valueOf(text.charAt(0)));
                        otp3.setText(String.valueOf(text.charAt(1)));
                        otp3.requestFocus();
                        otp3.setSelection(otp3.getText().length());
                    }
                    if (text.length() == 0){
                        otp1.requestFocus();
                        otp1.setSelection(otp1.getText().length());
                    }
                    break;
                case R.id.etOTP3:
                    if (text.length() > 1){
                        otp3.setText(String.valueOf(text.charAt(0)));
                        otp4.setText(String.valueOf(text.charAt(1)));
                        otp4.requestFocus();
                        otp4.setSelection(otp4.getText().length());
                    }
                    if (text.length() == 0){
                        otp2.requestFocus();
                        otp2.setSelection(otp2.getText().length());
                    }
                    break;

                case R.id.etOTP4:
                    if (text.length() > 1){
                        otp4.setText(String.valueOf(text.charAt(0)));
                        otp5.setText(String.valueOf(text.charAt(1)));
                        otp5.requestFocus();
                        otp5.setSelection(otp5.getText().length());
                    }
                    if (text.length() == 0){
                        otp3.requestFocus();
                        otp3.setSelection(otp3.getText().length());
                    }
                    break;


                case R.id.etOTP5:
                    if (text.length() > 1){
                        otp5.setText(String.valueOf(text.charAt(0)));
                        otp6.setText(String.valueOf(text.charAt(1)));
                        otp6.requestFocus();
                        otp6.setSelection(otp6.getText().length());
                    }
                    if (text.length() == 0){
                        otp4.requestFocus();
                        otp4.setSelection(otp4.getText().length());
                    }
                    break;

                case R.id.etOTP6:
                    if (text.length() == 0){
                        otp5.requestFocus();
                        otp5.setSelection(otp5.getText().length());
                    }
                    if (text.length() > 0){
                        String entered_otp = otp1.getText().toString() + otp2.getText().toString()
                                + otp3.getText().toString() + otp4.getText().toString()
                                + otp5.getText().toString() + otp6.getText().toString();

                        //verifyCode(entered_otp);
                    }
                    break;
            }
        }
        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    }
}