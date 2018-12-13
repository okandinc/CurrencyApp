package com.okandinc.testcase.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.okandinc.testcase.BuildConfig;
import com.okandinc.testcase.R;
import com.okandinc.testcase.ui.currencylist.CurrencyListFragment;
import com.okandinc.testcase.ui.exchangerate.ExchangeRatesDialogFragment;
import com.wang.avi.AVLoadingIndicatorView;

public class MainActivity extends AppCompatActivity {

    private AVLoadingIndicatorView avi;

    private FusedLocationProviderClient fusedLocationClient;

    private LocationCallback locationCallback;

    /**
     * RequestCode to use on Activity Result
     * after requesting to enable Device Location.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
        initLocationCallback();

        CurrencyListFragment currencyListFragment = new CurrencyListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container, currencyListFragment).commit();
    }

    private void setUpViews() {
        FloatingActionButton fabExchangeRates = findViewById(R.id.fab_common);
        avi = findViewById(R.id.avi);
        fabExchangeRates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExchangeRatesDialogFragment exchangeRatesDialogFragment = new ExchangeRatesDialogFragment();
                exchangeRatesDialogFragment.setCancelable(false);
                exchangeRatesDialogFragment.show(getSupportFragmentManager(),getString(R.string.exchange_rate_dialog_tag));
            }
        });
    }

    /**
     * Callback which receives location after request.
     */
    private void initLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    showToast(getString(R.string.activity_toast_msg_location_error));
                } else {
                    showToast(getString(R.string.activity_toast_msg_location_success));
                }
                Location location = null;
                for (Location locationFromUpdate : locationResult.getLocations()) {
                    location = locationFromUpdate;
                    break;
                }
                if (location != null) {
                    stopLocationUpdates();
                    sendEmail(location);
                }
            }
        };
    }

    /**
     * Checks Location Permission if it is granted or denied.
     * @return boolean value of Permission Status.
     */
    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Requests device location
     */
    @SuppressLint("MissingPermission")
    private void initializeLocationServices() {
        if (checkLocationPermission()) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        sendEmail(location);
                    } else {
                        LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(1000).setFastestInterval(5000);
                        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,null);
                        showToast(getString(R.string.activity_toast_msg_location_update));
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

    }

    /**
     * Request to enable Device Location If It is disabled.
     */
    private void turnLocationSettingOn() {
        LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(1000).setFastestInterval(5000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(MainActivity.this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            }
        });
    }

    /**
     * Stops getting updates after location successfully received
     */
    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    /**
     * Intents to Email Client with some information.
     * @param location Location of device.
     */
    private void sendEmail(Location location) {
        String s="info:";
        s += "\n Package Name: " + BuildConfig.APPLICATION_ID;
        s += "\n Min\\MaxSdkVersions: " + BuildConfig.MIN_SDK_VERSION + "\\" + BuildConfig.MAX_SDK_VERSION;
        s += "\n Longitute\\Latitude: " + location.getLongitude() + "\\" + location.getLatitude();
        Intent emailIntent =  new Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("application/image");
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n" + s);
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

    /**
     * Shows loading animation in case it is needed.
     */
    public void startAnim() {
        avi.setVisibility(View.VISIBLE);
        avi.show();
    }

    /**
     * Stops loading animation
     */
    public void stopAnim() {
        avi.hide();
    }

    /**
     * Shows a toast message on Screen when user needs to be informed.
     * @param msg string message.
     */
    private void showToast(String msg) {
        Toast.makeText(MainActivity.this,msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeLocationServices();
                } else {
                    showToast(getString(R.string.activity_toast_msg_location_permission_denied));
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        initializeLocationServices();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions_main,menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

        if (menuItem != null) {
            Drawable normalDrawable = menuItem.getIcon();
            Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
            DrawableCompat.setTint(wrapDrawable, getResources().getColor(R.color.colorWhite));

            menuItem.setIcon(wrapDrawable);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    initializeLocationServices();
                } else {
                    turnLocationSettingOn();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
