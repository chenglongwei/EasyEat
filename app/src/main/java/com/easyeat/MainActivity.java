package com.easyeat;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
    private Fragment fragment;
    private Fragment restaurantFragment;
    private Fragment favoriteFragment;
    private Fragment moreFragment;
    private FragmentManager fragmentManager;

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
        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        initNavigation();
        buildGoogleApiClient();
        setTitle(R.string.text_restaurants);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // Configure the search info and add any event listeners...
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fragment = restaurantFragment;
                fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
                ((RestaurantsFragment)restaurantFragment).submitQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void initNavigation() {
        // set default fragment
        restaurantFragment = new RestaurantsFragment();
        fragmentManager.beginTransaction().replace(R.id.main_container, restaurantFragment).commit();

        // set different fragment when navigation selected
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_restaurants:
                                // singleton navigation fragments
                                if (restaurantFragment == null) {
                                    restaurantFragment = new RestaurantsFragment();
                                }
                                fragment = restaurantFragment;
                                setTitle(R.string.text_restaurants);
                                break;
                            case R.id.action_favorites:
                                if (favoriteFragment == null) {
                                    favoriteFragment = new FavoritesFragment();
                                }
                                fragment = favoriteFragment;
                                setTitle(R.string.text_favorites);
                                break;
                            case R.id.action_more:
                                if (moreFragment == null) {
                                    moreFragment = new MoreFragment();
                                }
                                fragment = moreFragment;
                                setTitle(R.string.text_more);
                                break;
                        }

                        fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
                        return true;
                    }
                });
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
