package com.example.android.fueltracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements LocationListener
{

    private Button find_fuel;
    private View pulsing1;
    private View pulsing2;
    private View pulsing3;
    private ObjectAnimator objAnim1;
    private ObjectAnimator objAnim2;
    private ObjectAnimator objAnim3;
    private ValueAnimator profileButtonContract;
    private ValueAnimator profileButtonExpand;
    private int start;
    private AlphaAnimation animation1;
    private AlphaAnimation animation2;
    private AlphaAnimation animation3;
    private Button profile;
    private final int PERMISSION_REQUEST_CODE = 1;
    private Location currentLocation;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public static boolean isConnected;
    private boolean expanded = false;
    private LocationManager locationManager;
    private NetworkBroadcastReceiver networkBroadcastReceiver = new NetworkBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find_fuel = findViewById(R.id.findfuel);
        pulsing1 = findViewById(R.id.pulsing1);
        pulsing2 = findViewById(R.id.pulsing2);
        pulsing3 = findViewById(R.id.pulsing3);
        setAnimations();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(networkBroadcastReceiver, intentFilter);

        profile = findViewById(R.id.userProfile);
        start = profile.getLayoutParams().width;
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                profileButtonAnimation(view);

            }
        });

        find_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getLocation();
                String url = "http://api.mygasfeed.com/stations/radius/"
                        + currentLocation.getLatitude()
                        + "/" + currentLocation.getLongitude()
                        + "/5/reg/distance/e5ieinrc85.json?callback=?";
                Intent i = new Intent(MainActivity.this, StationsViewActivity.class);
                i.putExtra("REQUEST_URL", url);
                i.putExtra("INTERNET_CONNECTION", isConnected);
                //startActivity(i);
                if(isConnected)
                    startActivity(i);
                else
                    Toast.makeText(MainActivity.this, "NO CONNECTION", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        getLocation();

        if(!expanded)
        {
            profileButtonExpand = ValueAnimator.ofInt(profile.getWidth(), start);
            profileButtonExpand.setDuration(500);
            profileButtonExpand.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator)
                {
                    profile.getLayoutParams().width = (int) valueAnimator.getAnimatedValue();
                    profile.requestLayout();
                }
            });
            profileButtonExpand.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator)
                {
                    expanded = true;
                    profile.setText("PROFILE");
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            profileButtonExpand.start();
        }
        startPulseAnimation();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        stopPulseAnimation();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(networkBroadcastReceiver);
    }

    private void getLocation()
    {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
    }

    private void startPulseAnimation()
    {
        objAnim3.start();
        objAnim2.start();
        objAnim1.start();
    }

    private void stopPulseAnimation()
    {
        objAnim3.cancel();
        objAnim2.cancel();
        objAnim1.cancel();
    }

    private void setAnimations()
    {
        animation3 = new AlphaAnimation(1.0f, 0.0f);
        animation3.setDuration(3000);
        animation3.setStartOffset(500);
        animation3.setRepeatCount(Animation.INFINITE);
        animation3.setRepeatMode(ValueAnimator.RESTART);

        animation2 = new AlphaAnimation(1.0f, 0.0f);
        animation2.setDuration(3000);
        animation2.setStartOffset(1500);
        animation2.setRepeatCount(Animation.INFINITE);
        animation2.setRepeatMode(ValueAnimator.RESTART);

        animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(3000);
        animation1.setStartOffset(2500);
        animation1.setRepeatCount(Animation.INFINITE);
        animation1.setRepeatMode(ValueAnimator.RESTART);

        objAnim3= ObjectAnimator.ofPropertyValuesHolder(pulsing3,
                PropertyValuesHolder.ofFloat("scaleX", 2.0f),
                PropertyValuesHolder.ofFloat("scaleY", 2.0f));
        objAnim3.setDuration(3000);
        objAnim3.setStartDelay(500);
        objAnim3.setRepeatCount(ObjectAnimator.INFINITE);
        objAnim3.setRepeatMode(ValueAnimator.RESTART);
        objAnim3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator)
            {
                pulsing3.bringToFront();
                pulsing3.startAnimation(animation3);
            }

            @Override
            public void onAnimationEnd(Animator animator)
            {

            }

            @Override
            public void onAnimationCancel(Animator animator)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animator)
            {
                pulsing3.bringToFront();
                pulsing3.clearAnimation();
                animation3.setStartOffset(0);
                pulsing3.startAnimation(animation3);
            }
        });

        objAnim2= ObjectAnimator.ofPropertyValuesHolder(pulsing2,
                PropertyValuesHolder.ofFloat("scaleX", 2.0f),
                PropertyValuesHolder.ofFloat("scaleY", 2.0f));
        objAnim2.setDuration(3000);
        objAnim2.setStartDelay(1500);
        objAnim2.setRepeatCount(ObjectAnimator.INFINITE);
        objAnim2.setRepeatMode(ValueAnimator.RESTART);
        objAnim2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator)
            {
                pulsing2.bringToFront();
                pulsing2.startAnimation(animation2);
            }

            @Override
            public void onAnimationEnd(Animator animator)
            {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator)
            {
                pulsing2.bringToFront();
                pulsing2.clearAnimation();
                animation2.setStartOffset(0);
                pulsing2.startAnimation(animation2);
            }
        });

        objAnim1= ObjectAnimator.ofPropertyValuesHolder(pulsing1,
                PropertyValuesHolder.ofFloat("scaleX", 2.0f),
                PropertyValuesHolder.ofFloat("scaleY", 2.0f));
        objAnim1.setDuration(3000);
        objAnim1.setStartDelay(2500);
        objAnim1.setRepeatCount(ObjectAnimator.INFINITE);
        objAnim1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator)
            {
                pulsing1.bringToFront();
                pulsing1.startAnimation(animation1);
            }

            @Override
            public void onAnimationEnd(Animator animator)
            {

            }

            @Override
            public void onAnimationCancel(Animator animator)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animator)
            {
                pulsing1.bringToFront();
                pulsing1.clearAnimation();
                animation1.setStartOffset(0);
                pulsing1.startAnimation(animation1);
            }
        });

    }

    public void profileButtonAnimation(final View view)
    {
        profileButtonContract = ValueAnimator.ofInt(profile.getWidth(), 175);
        profileButtonContract.setDuration(500);
        profileButtonContract.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                profile.getLayoutParams().width = (int) valueAnimator.getAnimatedValue();
                profile.requestLayout();
            }
        });
        profileButtonContract.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator)
            {
                profile.setText("");
            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    expanded = false;
                    int revealX = (int) (view.getX() + (view.getWidth()/2));
                    int revealY = (int) (view.getY() + (view.getHeight()/2));
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    i.putExtra(ProfileActivity.CIRCLEX, revealX);
                    i.putExtra(ProfileActivity.CIRCLEY,revealY);
                    startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, view, "transition").toBundle());
                    overridePendingTransition(0, 0);
                }
                else
                {
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        profileButtonContract.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE: if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                                             Toast.makeText(MainActivity.this, "PERMISSIONS GRANTED", Toast.LENGTH_LONG).show(); break;

            default:    Toast.makeText(MainActivity.this, "PERMISSIONS NOT GRANTED", Toast.LENGTH_LONG).show(); break;
        }

    }

    @Override
    public void onLocationChanged(Location location) { currentLocation = location; }

    @Override
    public void onProviderDisabled(String provider) { Log.d("Latitude","disable"); }

    @Override
    public void onProviderEnabled(String provider) { Log.d("Latitude","enable");}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { Log.d("Latitude","status");}
}
