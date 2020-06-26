package net.ecommerceapp.LoginUI;

import android.content.Intent;
import android.os.Bundle;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.ecommerceapp.R;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    View view;
    TextInputEditText etMobileNumber;
    AppCompatButton btnContinue;
    SignInButton btnGoogleSignin;
    private TextInputLayout mTextInputLayout;

    GoogleSignInClient mGoogleSignInClient;
    public static int RC_SIGN_IN = 999;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

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

            Log.d(TAG, "updateUserDetailsOnDatabase: Done");


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Snackbar.make(view, "Error Occurred",Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            updateUserDetailsOnDatabase(task);
        }
    }
}