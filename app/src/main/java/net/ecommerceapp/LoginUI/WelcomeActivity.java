package net.ecommerceapp.LoginUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import net.ecommerceapp.R;

public class WelcomeActivity extends AppCompatActivity {

    AppCompatButton btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnContinue = findViewById(R.id.btn_continue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* LoginFragment loginFrag = new LoginFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_frame_layout, loginFrag)
                        .addToBackStack(null)
                        .commit();*/

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)
                        .replace(R.id.login_frame_layout, new LoginFragment()).addToBackStack(null).commit();
            }
        });
    }
}