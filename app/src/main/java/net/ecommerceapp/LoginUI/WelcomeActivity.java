package net.ecommerceapp.LoginUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.button.MaterialButton;

import net.ecommerceapp.R;

public class WelcomeActivity extends AppCompatActivity {

    MaterialButton btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnContinue = findViewById(R.id.btn_continue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                LoginFragment loginFrag = new LoginFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.login_frame_layout, loginFrag).addToBackStack(null).commit();
            }
        });
    }
}