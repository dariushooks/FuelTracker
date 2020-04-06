package com.example.android.fueltracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import static com.example.android.fueltracker.App.networkBroadcastReceiver;


public class MainActivity extends AppCompatActivity
{
    private Button find_fuel;
    private View pulsing1;
    private View pulsing2;
    private View pulsing3;
    private MotionLayout motionLayout;
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
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean expanded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MainFragment fragment = new MainFragment();
        transaction.add(R.id.fragmentContainer, fragment, "Main").commit();
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
}
