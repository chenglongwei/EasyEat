package com.easyeat;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.easyeat.adapter.ViewPagerAdapter;
import com.easyeat.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends BaseActivity implements
        ConnectionCallbacks, OnConnectionFailedListener {

    // For views
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private MenuItem prevMenuItem;
    private SearchView searchView;

    // fragments
    private Fragment restaurantFragment;
    private Fragment favoriteFragment;
    private Fragment myFragment;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.text_restaurants));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        initNavigation();

        buildGoogleApiClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        setupSearchView();
        return super.onCreateOptionsMenu(menu);
    }

    private void setupSearchView() {
        // Configure the search info and add any event listeners...
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewPager.setCurrentItem(0);
                ((RestaurantsFragment) restaurantFragment).submitQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initNavigation() {
        // set different fragment when navigation selected
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_restaurants:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_favorites:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_me:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                }

        );
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        restaurantFragment = new RestaurantsFragment();
        favoriteFragment = new FavoritesFragment();
        myFragment = new MyFragment();

        adapter.addFragment(restaurantFragment);
        adapter.addFragment(favoriteFragment);
        adapter.addFragment(myFragment);
        viewPager.setAdapter(adapter);

        // https://developer.android.com/reference/android/support/v4/view/ViewPager.html#setOffscreenPageLimit(int)
        // retain 2 pages in memory
        viewPager.setOffscreenPageLimit(2);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                MenuItem item = bottomNavigationView.getMenu().getItem(position);

                setTitle(item.getTitle());
                item.setChecked(true);
                prevMenuItem = item;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // If you want to remove sliding of fragments, add following;
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Config.latitude = mLastLocation.getLatitude();
            Config.longtitude = mLastLocation.getLongitude();
            Log.d("latitude: " + Config.latitude);
            Log.d("longitude: " + Config.longtitude);
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(Config.TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(Config.TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
}
