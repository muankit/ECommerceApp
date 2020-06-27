package net.ecommerceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.ecommerceapp.Home.MainActivity;
import net.ecommerceapp.LoginUI.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

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
                        sleep(3000);
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
                        sleep(3000);

                        Intent loginIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
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