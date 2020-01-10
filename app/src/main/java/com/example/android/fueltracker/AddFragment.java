package com.example.android.fueltracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.android.fueltracker.data.UserContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFragment extends Fragment
{

    public static final String CIRCLEX = "X";
    public static final String CIRCLEY = "Y";
    View rootLayout;
    private int revealX;
    private int revealY;
    private EditText spent;
    private EditText price;
    private EditText station;
    private Button add;
    private Button cancel;
    private FloatingActionButton fab;
    private float finalRadius;
    private Animator circularReveal;
    private Animator circularExit;
    private ValueAnimator colorReveal;
    private ValueAnimator colorExit;
    private int color1;
    private int color2;
    DateFormat dateFormat;
    Date date;
    private Intent intent;
    View rootView;

    public AddFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        rootView = inflater.inflate(R.layout.activity_add, container, false);
        intent = getActivity().getIntent();
        rootLayout = rootView.findViewById(R.id.root);

        add = rootView.findViewById(R.id.saveAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(errorMessage(spent, price, station))
                {
                    writeToDatabase();
                    circularExit.start();
                    //colorExit.start();
                }
            }
        });

        cancel = rootView.findViewById(R.id.cancelAdd);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                circularExit.start();
                fab.setVisibility(View.VISIBLE);
                //colorExit.start();
            }
        });

        fab = rootView.findViewById(R.id.addEntryFab);

        rootLayout.post(new Runnable() {
            @Override
            public void run()
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && intent.hasExtra(CIRCLEX) && intent.hasExtra(CIRCLEY))
                {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
                    revealX = intent.getIntExtra(CIRCLEX, 0);
                    revealY = intent.getIntExtra(CIRCLEY, 0);
                    color1 = getResources().getColor(R.color.colorPrimary);
                    color2 = getResources().getColor(R.color.white);

                    colorReveal = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2);
                    colorReveal.setDuration(2000);
                    colorReveal.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator)
                        {
                            rootLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });

                    colorExit = ValueAnimator.ofObject(new ArgbEvaluator(), color2, color1);
                    colorExit.setDuration(2000);
                    colorExit.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator)
                        {
                            rootLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });

                    circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, revealX, revealY, 0, finalRadius);
                    circularReveal.setDuration(1000);
                    circularReveal.setInterpolator(new AccelerateInterpolator());
                    circularReveal.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            fab.setVisibility(View.GONE);
                            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Entry");
                            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                    });

                    circularExit = ViewAnimationUtils.createCircularReveal(rootLayout, revealX, revealY, finalRadius, 0);
                    circularExit.setDuration(1000);
                    circularExit.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            rootLayout.setVisibility(View.INVISIBLE);
                            getActivity().finish();
                            getActivity().overridePendingTransition(0,0);
                        }
                    });

                    ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
                    if (viewTreeObserver.isAlive()) {
                        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout()
                            {
                                rootLayout.setVisibility(View.VISIBLE);
                                circularReveal.start();
                                //colorReveal.start();
                                rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });
                    }
                }
                else
                {
                    rootLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        spent = rootView.findViewById(R.id.spentInput);
        price = rootView.findViewById(R.id.priceInput);
        station = rootView.findViewById(R.id.stationInput);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();

        return rootView;
    }


    private boolean errorMessage(EditText spentText, EditText priceText, EditText stationText)
    {
        boolean notEmpty = true;
        String Spent = spentText.getText().toString().trim();
        String Price = priceText.getText().toString().trim();
        String Station = stationText.getText().toString().trim();

        if(TextUtils.isEmpty(Spent))
        {
            spent.setError("FIELD CANNOT BE EMPTY");
            notEmpty = false;
        }

        if(TextUtils.isEmpty(Price))
        {
            price.setError("FIELD CANNOT BE EMPTY");
            notEmpty = false;
        }

        if(TextUtils.isEmpty(Station))
        {
            station.setError("FIELD CANNOT BE EMPTY");
            notEmpty = false;
        }
        return notEmpty;
    }

    private void writeToDatabase()
    {
        DecimalFormat df = new DecimalFormat("#.##");
        String spentString = spent.getText().toString().trim();
        String priceString = price.getText().toString().trim();
        String gallonsString = Float.toString(Float.valueOf(df.format(Float.parseFloat(spentString)/Float.parseFloat(priceString)))).trim();
        String stationString = station.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(UserContract.GasEntry.COLUMN_SPENT, spentString);
        values.put(UserContract.GasEntry.COLUMN_GALLONS, gallonsString);
        values.put(UserContract.GasEntry.COLUMN_PRICE, priceString);
        values.put(UserContract.GasEntry.COLUMN_STATION, stationString.toUpperCase());
        values.put(UserContract.GasEntry.COLUMN_DATE, dateFormat.format(date));

        getActivity().getContentResolver().insert(UserContract.GasEntry.CONTENT_URI, values);
    }
}
