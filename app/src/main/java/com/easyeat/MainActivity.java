package com.easyeat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private Fragment restaurantFragment;
    private Fragment favoriteFragment;
    private Fragment moreFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        initNavigation();
    }

    private void initNavigation() {
        // singleton navigation fragments
        if (restaurantFragment == null) {
            restaurantFragment = new RestaurantsFragment();
        }
        if (favoriteFragment == null) {
            favoriteFragment = new FavoritesFragment();
        }
        if (moreFragment == null) {
            moreFragment = new MoreFragment();
        }

        // set default fragment
        fragmentManager.beginTransaction().replace(R.id.main_container, restaurantFragment).commit();

        // set different fragment when navigation selected
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_restaurants:
                                fragment = restaurantFragment;
                                break;
                            case R.id.action_favorites:
                                fragment = favoriteFragment;
                                break;
                            case R.id.action_more:
                                fragment = moreFragment;
                                break;
                        }

                        fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
                        return true;
                    }
                });
    }
}
