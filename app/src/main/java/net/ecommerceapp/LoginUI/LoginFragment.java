package net.ecommerceapp.LoginUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import net.ecommerceapp.Home.MainActivity;
import net.ecommerceapp.R;
import net.ecommerceapp.Utils.DeviceUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginFragment extends Fragment {

    View view;
    TextInputEditText etMobileNumber;
    AppCompatButton btnContinue;
    SignInButton btnGoogleSignin;
    TextInputLayout mTextInputLayout;

    GoogleSignInClient mGoogleSignInClient;
    public static int RC_SIGN_IN = 999;

    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        db = FirebaseFirestore.getInstance();

        init(view);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(container.getContext(), gso);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etMobileNumber.getText().length() != 10) {
                    mTextInputLayout.setError("Check Mobile Number");
                    //  etMobileNumber.requestFocus();
                }
                else {
                    mTextInputLayout.setError(null);
                    //otp verification fragment
                    Bundle b = new Bundle();
                    b.putString("EnteredMobile",etMobileNumber.getText().toString());
                    OTPVerificationFragment otpVerificationFragment = new OTPVerificationFragment();
                    otpVerificationFragment.setArguments(b);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)
                            .replace(R.id.login_frame_layout, otpVerificationFragment).addToBackStack(null).commit();
                }
            }
        });

        btnGoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        return view;
    }

    private void init(View view) {

        etMobileNumber = view.findViewById(R.id.et_mobile_number);
        btnContinue = view.findViewById(R.id.btn_continue);
        btnGoogleSignin = view.findViewById(R.id.btn_sign_in_with_google);
        mTextInputLayout = view.findViewById(R.id.textInputLayout);
    }

    private void updateUserDetailsOnDatabase(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            String profileImageUrl = "", userName = "", userEmail = "";
            if(account.getPhotoUrl()!=null)
                profileImageUrl = account.getPhotoUrl().toString();
            userName = account.getDisplayName();
            userEmail = account.getEmail();

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", userName);
            userData.put("email", userEmail);
            userData.put("profileImage",profileImageUrl);
            userData.put("registeredWith","Gmail");
            userData.put("deviceIMEI", DeviceUtils.getDeviceIMEI(view.getContext()));
            userData.put("deviceName",DeviceUtils.getDeviceName());
            userData.put("deviceVersion",DeviceUtils.getDeviceVersion());
            userData.put("rootStatus",DeviceUtils.isDeviceRooted());
            userData.put("accountCreationTime", FieldValue.serverTimestamp());

            firebaseAuthWithGoogle(account.getIdToken(),userData);

        } catch (ApiException e) {
            Snackbar.make(view, "Error Occurred",Snackbar.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken,final Map<String, Object> userData) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener((Activity) view.getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //FirebaseUser user = mAuth.getCurrentUser();

                            final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            db.collection("Users").whereEqualTo("Email",userData.get("Email")).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(Objects.requireNonNull(task.getResult()).size() > 0){
                                                //User Already Exist

                                                Log.d("accountcreation", "onComplete: user exist");

                                                db.collection("Users").document(currentUser).update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Intent googleSigninIntent = new Intent(view.getContext(), MainActivity.class);
                                                        googleSigninIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(googleSigninIntent);
                                                    }
                                                });

                                            }else{
                                                //add a new user to Firestore database
                                                Log.d("accountcreation", "onComplete: user not exist");

                                                db.collection("Users").document(currentUser).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Intent googleSigninIntent = new Intent(view.getContext(), MainActivity.class);
                                                        googleSigninIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(googleSigninIntent);
                                                    }
                                                });
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            updateUserDetailsOnDatabase(task);
        }
    }
}