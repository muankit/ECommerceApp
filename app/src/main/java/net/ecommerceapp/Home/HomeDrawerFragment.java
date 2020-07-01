package net.ecommerceapp.Home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import net.ecommerceapp.R;

public class HomeDrawerFragment extends Fragment {

    private static final String TAG = "HomeDrawerFragment";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    
    private TextView mHomeText;
    private ImageButton mDrawerCloseBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_drawer_layout, container, false);
        
        init(view);

        mDrawerCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });

        return view;
    }

    private void init(View view) {

        mDrawerCloseBtn = (ImageButton) view.findViewById(R.id.drawer_close_btn);


        mHomeText = view.findViewById(R.id.drawer_home_text);
    }

    /**
     * Created by xaif 28 June 2020
     * Method to setup custom drawer using fragment
     * @param fragmentId
     * @param drawerLayout
     * @param toolbar
     */
    public void setUpChildDrawer(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {

        mDrawerLayout = drawerLayout;
        mToolbar = toolbar;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.drawer_menu);

        mDrawerLayout.addDrawerListener(mDrawerToggle);


        mDrawerLayout.post(() -> mDrawerToggle.syncState());

    }
}
