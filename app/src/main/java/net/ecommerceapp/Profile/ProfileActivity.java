package net.ecommerceapp.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import net.ecommerceapp.R;

import at.blogc.android.views.ExpandableTextView;

public class ProfileActivity extends AppCompatActivity {

    private ExpandableTextView mAddressText;
    private Toolbar mToolbar;

    private TextView mName, mPhone , mEmail , mAddress , mCity , mGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        setUpToolbar();

        mAddressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddressText.toggle();
            }
        });

    }

    private void setUpToolbar() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_24);
    }

    private void init() {
        mAddressText = (ExpandableTextView) findViewById(R.id.profile_address);
        mAddressText.setInterpolator(new OvershootInterpolator());

        mToolbar = (Toolbar)  findViewById(R.id.profile_toolbar);

        mName = (TextView) findViewById(R.id.profile_name);
        mPhone = (TextView) findViewById(R.id.profile_phone);
        mEmail = (TextView) findViewById(R.id.profile_email);
        mCity = (TextView) findViewById(R.id.profile_city);
        mGender = (TextView) findViewById(R.id.profile_gender);
    }
}