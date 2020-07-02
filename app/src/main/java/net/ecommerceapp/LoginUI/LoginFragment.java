package net.ecommerceapp.LoginUI;

import android.app.Activity;
import android.app.ProgressDialog;
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

    ProgressDialog mProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        db = FirebaseFirestore.getInstance();

        init(view);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
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

        mProgress = new ProgressDialog(view.getContext());
    }

    private Map<String, Object> generateUserDataMap(GoogleSignInAccount account) {

        Map<String, Object> generatedMap = new HashMap<>();
        String profileImageUrl = "", userName = "", userEmail = "";
        if(account.getPhotoUrl()!=null)
            profileImageUrl = account.getPhotoUrl().toString();
        userName = account.getDisplayName();
        userEmail = account.getEmail();

        generatedMap.put("name", userName);
        generatedMap.put("email", userEmail);
        generatedMap.put("profileImage",profileImageUrl);
        generatedMap.put("registeredWith","Gmail");
        generatedMap.put("deviceIMEI", DeviceUtils.getDeviceIMEI(view.getContext()));
        generatedMap.put("deviceName",DeviceUtils.getDeviceName());
        generatedMap.put("deviceVersion",DeviceUtils.getDeviceVersion());
        generatedMap.put("rootStatus",DeviceUtils.isDeviceRooted());
        generatedMap.put("accountCreationTime", FieldValue.serverTimestamp());

        return generatedMap;
    }

    private void firebaseAuthWithGoogle(String idToken , GoogleSignInAccount account) {

        Map<String, Object> userData = generateUserDataMap(account);

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
                                                db.collection("Users").document(currentUser).update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        mProgress.dismiss();
                                                        Intent googleSigninIntent = new Intent(view.getContext(), MainActivity.class);
                                                        googleSigninIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(googleSigninIntent);
                                                    }
                                                });

                                            }else{
                                                //add a new user to Firestore database
                                                db.collection("Users").document(currentUser).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        mProgress.dismiss();
                                                        Intent googleSigninIntent = new Intent(view.getContext(), MainActivity.class);
                                                        googleSigninIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null) {
                    firebaseAuthWithGoogle(account.getIdToken(), account);

                    mProgress.setTitle("Logging you in");
                    mProgress.setMessage("Please wait while we check the details and get your account ready...");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Snackbar.make(view, "Error Occurred",Snackbar.LENGTH_LONG).show();
                // ...
            }
        }

    }
}