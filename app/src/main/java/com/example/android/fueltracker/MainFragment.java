package com.example.android.fueltracker;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainFragment extends Fragment
{
    private View rootView;
    private Button find_fuel;
    private Button profile;
    private MotionLayout motionLayout;

    private View pulsing1;
    private View pulsing2;
    private View pulsing3;
    private ObjectAnimator objAnim1;
    private ObjectAnimator objAnim2;
    private ObjectAnimator objAnim3;
    private AlphaAnimation animation1;
    private AlphaAnimation animation2;
    private AlphaAnimation animation3;

    private boolean expanded = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if(expanded)
        {
            rootView = getLayoutInflater().inflate(R.layout.fragment_main_expanded, container, false);
            expanded = true;
        }

        else
        {
            rootView = getLayoutInflater().inflate(R.layout.fragment_main_contracted, container, false);
            expanded = false;
        }

        motionLayout = rootView.findViewById(R.id.mainLayout);
        find_fuel = rootView.findViewById(R.id.findfuel);
        pulsing1 = rootView.findViewById(R.id.pulsing1);
        pulsing2 = rootView.findViewById(R.id.pulsing2);
        pulsing3 = rootView.findViewById(R.id.pulsing3);
        setAnimations();

        profile = rootView.findViewById(R.id.userProfile);

        find_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                StationsViewFragment fragment = new StationsViewFragment();
                transaction.replace(R.id.fragmentContainer, fragment, null).addToBackStack("").commit();
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
                if(expanded)
                {
                    expanded = false;
                    profileButtonAnimation(profile);
                }

                else
                    expanded = true;

            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v)
            {

            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        startPulseAnimation();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        stopPulseAnimation();
    }

    private void startPulseAnimation()
    {
        objAnim3.start();
        objAnim2.start();
        objAnim1.start();
    }

    private void stopPulseAnimation()
    {
        objAnim3.end();
        objAnim2.end();
        objAnim1.end();
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

    private void profileButtonAnimation(final View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            int revealX = (int) (view.getX() + (view.getWidth()/2));
            int revealY = (int) (view.getY() + (view.getHeight()/2));
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            ProfileFragment fragment = new ProfileFragment(revealX, revealY);
            transaction.replace(R.id.fragmentContainer, fragment, null).addToBackStack("").commit();
        }
    }
}
