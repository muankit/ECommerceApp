package net.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.ecommerceapp.LoginUI.LoginActivity;
import net.ecommerceapp.Utils.DeviceUtils;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        boolean isUserExists = checkUser();

        if (isUserExists) {

            Thread myThread1 = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(500);

                        // TODO change main activity to another here
                        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(loginIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };

            myThread1.start();
        }else{
            Thread myThread2 = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(500);

                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            myThread2.start();
        }
    }

    private boolean checkUser() {

        FirebaseUser user = mAuth.getCurrentUser();

        return user != null;
    }
}