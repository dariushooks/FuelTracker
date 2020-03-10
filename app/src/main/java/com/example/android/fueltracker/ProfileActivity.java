package com.example.android.fueltracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.fueltracker.data.UserContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements GasTrackerRecyclerAdapter.ListItemClickListener
{
    private ArrayList<GasTracker> tracker = new ArrayList<>();
    private GasTrackerRecyclerAdapter trackerRecyclerAdapter;
    private RecyclerView recyclerView;

    public static final String CIRCLEX = "X";
    public static final String CIRCLEY = "Y";
    private int revealX;
    private int revealY;
    private int color1;
    private int color2;
    private float finalRadius;
    private Intent intent;
    private Animator circularReveal;
    private Animator circularExit;
    private ValueAnimator colorReveal;
    private ValueAnimator colorExit;

    private ConstraintLayout profile;
    private RelativeLayout current;
    private RelativeLayout edit;
    private LinearLayout delete;

    private TextView date;
    private TextView spent;
    private TextView price;
    private TextView gallons;
    private TextView station;
    private TextView emptyList;

    private EditText editSpent;
    private EditText editPrice;
    private EditText editStation;

    private Button editButton;
    private Button deleteButton;
    private Button doneButton;
    private Button cancelButton;
    private Button saveButton;
    private Button yesButton;
    private Button noButton;

    private FloatingActionButton fabMain;

    private String rowID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_constraint);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        profile = findViewById(R.id.profile);
        emptyList = findViewById(R.id.noData);
        intent = getIntent();

        fabMain = findViewById(R.id.mainFab);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                insertData(view);
            }
        });

        recyclerView = findViewById(R.id.historyListRecycler);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(getDrawable(R.drawable.history_list_divider));
        recyclerView.addItemDecoration(divider);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        trackerRecyclerAdapter = new GasTrackerRecyclerAdapter(this, tracker);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(trackerRecyclerAdapter);

        profile.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && intent.hasExtra(CIRCLEX) && intent.hasExtra(CIRCLEY))
                {
                    profile.setVisibility(View.INVISIBLE);
                    finalRadius = (float) (Math.max(profile.getWidth(), profile.getHeight()) * 1.1);
                    revealX = intent.getIntExtra(CIRCLEX, 0);
                    revealY = intent.getIntExtra(CIRCLEY, 0);
                    color1 = getResources().getColor(R.color.black);
                    color2 = getResources().getColor(R.color.white);

                    colorReveal = ValueAnimator.ofObject(new ArgbEvaluator(), color2, color1);
                    colorReveal.setDuration(1000);
                    colorReveal.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator)
                        {
                            profile.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });

                    colorExit = ValueAnimator.ofObject(new ArgbEvaluator(), color1, color2);
                    colorExit.setDuration(1000);
                    colorExit.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator)
                        {
                            profile.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });

                    circularReveal = ViewAnimationUtils.createCircularReveal(profile, revealX, revealY, 0, finalRadius);
                    circularReveal.setDuration(1000);
                    circularReveal.setInterpolator(new AccelerateInterpolator());
                    circularReveal.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {

                        }
                    });

                    circularExit = ViewAnimationUtils.createCircularReveal(profile, revealX, revealY, finalRadius, 0);
                    circularExit.setDuration(1000);
                    circularExit.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            profile.setVisibility(View.INVISIBLE);
                            finish();
                            overridePendingTransition(0,0);
                        }
                    });

                    ViewTreeObserver viewTreeObserver = profile.getViewTreeObserver();
                    if (viewTreeObserver.isAlive())
                    {
                        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout()
                            {
                                profile.setVisibility(View.VISIBLE);
                                circularReveal.start();
                                colorReveal.start();
                                profile.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });
                    }
                }
                else
                    profile.setVisibility(View.VISIBLE);
            }

        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        tracker.clear();
        readFromDatabase();
        if(tracker.isEmpty())
            emptyList.setVisibility(View.VISIBLE);
        else
        {
            emptyList.setVisibility(View.GONE);
            trackerRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private boolean errorMessage(EditText spentText, EditText priceText, EditText stationText)
    {
        boolean notEmpty = true;
        String Spent = spentText.getText().toString().trim();
        String Price = priceText.getText().toString().trim();
        String Station = stationText.getText().toString().trim();

        if(TextUtils.isEmpty(Spent))
        {
            editSpent.setError("FIELD CANNOT BE EMPTY");
            notEmpty = false;
        }

        if(TextUtils.isEmpty(Price))
        {
            editPrice.setError("FIELD CANNOT BE EMPTY");
            notEmpty = false;
        }

        if(TextUtils.isEmpty(Station))
        {
            editStation.setError("FIELD CANNOT BE EMPTY");
            notEmpty = false;
        }
        return notEmpty;
    }

    @Override
    public void onBackPressed()
    {
        circularExit.start();
        colorExit.start();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void openDialog(int i, View view)
    {
        final int position = i;
        final int revealX = (view.getWidth()/2);
        final int revealY = (view.getHeight()/2);
        final View alertLayout = getLayoutInflater().inflate(R.layout.profile_alert_dialog,null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();


        editButton = alertLayout.findViewById(R.id.editButton);
        deleteButton = alertLayout.findViewById(R.id.deleteButton);
        doneButton = alertLayout.findViewById(R.id.doneButton);
        cancelButton = alertLayout.findViewById(R.id.cancelButton);
        saveButton = alertLayout.findViewById(R.id.saveButton);
        yesButton = alertLayout.findViewById(R.id.yesButton);
        noButton = alertLayout.findViewById(R.id.noButton);

        current = alertLayout.findViewById(R.id.currentLayout);
        edit = alertLayout.findViewById(R.id.editLayout);
        delete = alertLayout.findViewById(R.id.deleteEntry);

        date = alertLayout.findViewById(R.id.currentDate);
        date.setText(tracker.get(position).getDate());

        spent = alertLayout.findViewById(R.id.currentSpent);
        spent.setText(tracker.get(position).getSpent());

        gallons = alertLayout.findViewById(R.id.currentGallons);
        gallons.setText(tracker.get(position).getGallons());

        price = alertLayout.findViewById(R.id.currentPrice);
        price.setText(tracker.get(position).getPrice());

        station = alertLayout.findViewById(R.id.currentStation);
        station.setText(tracker.get(position).getStation());

        editSpent = alertLayout.findViewById(R.id.editSpent);
        editPrice = alertLayout.findViewById(R.id.editPrice);
        editStation = alertLayout.findViewById(R.id.editStation);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,1000);
                current.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                editSpent.setText(tracker.get(position).getSpent());
                editPrice.setText(tracker.get(position).getPrice());
                editStation.setText(tracker.get(position).getStation());
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                float finalRadius = (float) (Math.max(alertLayout.getWidth(), alertLayout.getHeight()) * 1.1);

                // create the animator for this view (the start radius is zero)
                Animator circularReveal = ViewAnimationUtils.createCircularReveal(alertLayout, revealX, revealY, finalRadius, 0);
                circularReveal.setDuration(1000);
                circularReveal.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        alertLayout.setVisibility(View.INVISIBLE);
                        alertDialog.dismiss();
                    }
                });
                // make the view visible and start the animation
                circularReveal.start();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                current.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deleteFromDatabase();
                alertDialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                current.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.VISIBLE);
                doneButton.setVisibility(View.VISIBLE);
                delete.setVisibility(View.GONE);
                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(errorMessage(editSpent, editPrice, editStation))
                {
                    DecimalFormat df = new DecimalFormat("#.##");
                    alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,700);
                    current.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);
                    editButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                    doneButton.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    spent.setText(editSpent.getText().toString().trim());
                    price.setText(editPrice.getText().toString().trim());
                    gallons.setText(Float.toString(Float.parseFloat(df.format(Float.parseFloat(spent.getText().toString().trim())/Float.parseFloat(price.getText().toString().trim())))).trim());
                    station.setText(editStation.getText().toString().toUpperCase().trim());
                    writeToDatabase();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,700);
                current.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                doneButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
            }
        });


        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                tracker.clear();
                readFromDatabase();
                if(tracker.isEmpty())
                    emptyList.setVisibility(View.VISIBLE);
                else
                {
                    emptyList.setVisibility(View.GONE);
                    trackerRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                float finalRadius = (float) (Math.max(alertLayout.getWidth(), alertLayout.getHeight()) * 1.1);

                // create the animator for this view (the start radius is zero)
                Animator circularReveal = ViewAnimationUtils.createCircularReveal(alertLayout, revealX, revealY, 0, finalRadius);
                circularReveal.setDuration(1000);
                circularReveal.setInterpolator(new AccelerateInterpolator());

                // make the view visible and start the animation
                alertLayout.setVisibility(View.VISIBLE);
                circularReveal.start();
            }
        });

        alertDialog.setView(alertLayout);
        new Dialog(this.getApplicationContext());
        alertDialog.show();
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        alertDialog.getWindow().setBackgroundDrawable(inset);
        //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setElevation(20.0f);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,700);
    }

    private void insertData(View view)
    {
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            // Apply activity transition
            int revealX = (int) (view.getX() + (view.getWidth()/2));
            int revealY = (int) (view.getY() + (view.getHeight()/2));
            Intent i = new Intent(ProfileActivity.this, AddActivity.class);
            i.putExtra(AddFragment.CIRCLEX, revealX);
            i.putExtra(AddFragment.CIRCLEY,revealY);
            startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition").toBundle());
            overridePendingTransition(0, 0);
        }
        else
        {
        }
    }

    private void readFromDatabase()
    {
        // Create database helper
        //UserDbHelper mDbHelper = new UserDbHelper(this);

        // Gets the database in read mode
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {UserContract.GasEntry.COLUMN_DATE, UserContract.GasEntry.COLUMN_SPENT, UserContract.GasEntry.COLUMN_GALLONS,
                                UserContract.GasEntry.COLUMN_PRICE, UserContract.GasEntry.COLUMN_STATION, UserContract.GasEntry.COLUMN_ID};
        Cursor c = getContentResolver().query(UserContract.GasEntry.CONTENT_URI, projection, null, null, null);
        //db.query(UserContract.GasEntry.TABLE_NAME, projection, null, null, null, null, null);

        try
        {
            int dateIndex = c.getColumnIndex(UserContract.GasEntry.COLUMN_DATE);
            int spentIndex = c.getColumnIndex(UserContract.GasEntry.COLUMN_SPENT);
            int gallonsIndex = c.getColumnIndex(UserContract.GasEntry.COLUMN_GALLONS);
            int priceIndex = c.getColumnIndex(UserContract.GasEntry.COLUMN_PRICE);
            int stationIndex = c.getColumnIndex(UserContract.GasEntry.COLUMN_STATION);
            int idIndex = c.getColumnIndex(UserContract.GasEntry.COLUMN_ID);
            if(c.moveToFirst())
                do
                {
                    String id = c.getString(idIndex);
                    String date = c.getString(dateIndex);
                    String spent = c.getString(spentIndex);
                    String gallons = c.getString(gallonsIndex);
                    String price = c.getString(priceIndex);
                    String station = c.getString(stationIndex);
                    tracker.add(new GasTracker(id, date, spent, gallons, price, station));
                }while (c.moveToNext());
        }

        finally
        {
            c.close();
        }
    }

    private void writeToDatabase()
    {
        DecimalFormat df = new DecimalFormat("#.##");
        String dateString = date.getText().toString().trim();
        String spentString = spent.getText().toString().trim();
        String priceString = price.getText().toString().trim();
        String gallonsString = Float.toString(Float.parseFloat(df.format(Float.parseFloat(spentString)/Float.parseFloat(priceString)))).trim();
        String stationString = station.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(UserContract.GasEntry.COLUMN_DATE, dateString);
        values.put(UserContract.GasEntry.COLUMN_SPENT, spentString);
        values.put(UserContract.GasEntry.COLUMN_GALLONS, gallonsString);
        values.put(UserContract.GasEntry.COLUMN_PRICE, priceString);
        values.put(UserContract.GasEntry.COLUMN_STATION, stationString.toUpperCase());

         String where = UserContract.GasEntry.COLUMN_ID;
         String w[] = {rowID};
         Uri uri = Uri.withAppendedPath(UserContract.GasEntry.CONTENT_URI,rowID);

         getContentResolver().update(uri, values, where, w);
    }

    private void deleteFromDatabase()
    {
        String where = UserContract.GasEntry.COLUMN_ID ;
        String[] w = {rowID};
        Uri uri = Uri.withAppendedPath(UserContract.GasEntry.CONTENT_URI,rowID);

        getContentResolver().delete(uri, where, w);
    }


    @Override
    public void onClick(int position, View view)
    {
        rowID = tracker.get(position).getId();
        openDialog(position, view);
    }
}