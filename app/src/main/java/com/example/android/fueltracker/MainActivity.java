package com.example.android.fueltracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.app.ActivityOptionsCompat;
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
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main_container);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MainFragment fragment = new MainFragment();
        transaction.add(R.id.fragmentContainer, fragment, "Main").commit();

        /*motionLayout = findViewById(R.id.mainLayout);
        find_fuel = findViewById(R.id.findfuel);
        pulsing1 = findViewById(R.id.pulsing1);
        pulsing2 = findViewById(R.id.pulsing2);
        pulsing3 = findViewById(R.id.pulsing3);
        setAnimations();

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
                Intent i = new Intent(MainActivity.this, StationsViewActivity.class);
                startActivity(i);
            }
        });

        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1)
            {
                profile.setText("");
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v)
            {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i)
            {
                if(motionLayout.getCurrentState() == motionLayout.getEndState())
                    profileButtonAnimation(profile);
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v)
            {

            }
        });*/
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

        /*if(!expanded)
        {
            expanded = true;
            motionLayout.transitionToStart();
        }

        startPulseAnimation();*/
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //stopPulseAnimation();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            expanded = false;
            int revealX = (int) (view.getX() + (view.getWidth()/2));
            int revealY = (int) (view.getY() + (view.getHeight()/2));
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            ProfileFragment fragment = new ProfileFragment(revealX, revealY);
            transaction.replace(R.id.fragmentContainer, fragment, null).addToBackStack("").commit();

            /*Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            i.putExtra(ProfileActivity.CIRCLEX, revealX);
            i.putExtra(ProfileActivity.CIRCLEY,revealY);
            startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, view, "transition").toBundle());
            overridePendingTransition(0, 0);*/
        }

        else
        {
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(i);
        }
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
