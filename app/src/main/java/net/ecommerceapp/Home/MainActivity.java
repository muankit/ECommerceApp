package net.ecommerceapp.Home;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import net.ecommerceapp.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;

    private Toolbar mToolbar;
    private TextView mCategorySeeAll;

   // BottomSheetBehavior sheetBehavior;
   // ConstraintLayout mHomeCategorySheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // init();

        setUpToolbar();

        setUpNavigationDrawer();

        /*mCategorySeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager()

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                   // btnBottomSheet.setText("Close sheet");
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                   // btnBottomSheet.setText("Expand sheet");
                }
            }
        });*/
    }

/*    private void init() {

        mCategorySeeAll = (TextView) findViewById(R.id.home_category_see_all_text);

        mHomeCategorySheet = (ConstraintLayout) findViewById(R.id.home_category_sheet);

        sheetBehavior = BottomSheetBehavior.from(mHomeCategorySheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                      //  btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                       // btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }*/

    private void setUpToolbar() {
        mToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.main_home_toolbar);
        setSupportActionBar(mToolbar);
    }

    private void setUpNavigationDrawer() {

        HomeDrawerFragment homeDrawerFragment = (HomeDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.main_home_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        homeDrawerFragment.setUpChildDrawer(R.id.main_home_drawer, mDrawerLayout, mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}