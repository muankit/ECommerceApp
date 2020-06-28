package net.ecommerceapp.Home;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import net.ecommerceapp.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        setUpNavigationDrawer();

    }

    private void setUpToolbar() {
        mToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.main_home_toolbar);
        setSupportActionBar(mToolbar);
    }

    private void setUpNavigationDrawer() {

        HomeDrawerFragment homeDrawerFragment = (HomeDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.main_home_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        homeDrawerFragment.setUpChildDrawer(R.id.main_home_drawer, mDrawerLayout, mToolbar);
    }
}