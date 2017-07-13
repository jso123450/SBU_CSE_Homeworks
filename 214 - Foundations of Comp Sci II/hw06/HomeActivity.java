package com.example.bitterplatform.activities;

/**
 * Johnny So
 * 111158276
 * Homework #6
 * CSE 214 Recitation 12
 * Recitation TA: Charles Chen
 * Grading TA: Timothy Zhang
 * @author Johnny
 */

/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

// Project Imports
import com.example.bitterplatform.R;
import com.example.bitterplatform.fragments.MenuFragment;
import com.example.bitterplatform.source.Account;
import com.example.bitterplatform.source.Bitter;


/* ----------------------------- CLASS DEF ----------------------------- */

public class HomeActivity extends AppCompatActivity {


    /* ----------------------- VARIABLES ----------------------- */

    private static final String TAG = "HomeActivity";

    // LOGIC & DATA VARS
    private Bitter bitter;
    private Account account;

    // FRAGMENT VARS
    private FragmentManager fragmentManager;
    private MenuFragment menuFragment;

    /* ----------------------- ACTIVITY LIFECYCLE METHODS ----------------------- */
    /**
     * Required activity lifecycle method. Performs things that should only happen once, in the
     * beginning of the activity.
     * @param savedInstanceState the instance state containing prev details about this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // sets the layout and calls the super's onCreate
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_home);

        showUpButton();

        // grab the Bitter object passed through the intent
        Intent intent = getIntent();
        bitter = (Bitter) intent.getSerializableExtra("Bitter");

        // grab the current account
        String email = intent.getStringExtra("Email");
        account = bitter.getAccount(email);

        // setup the fragment vars
        fragmentManager = getSupportFragmentManager();
        menuFragment = new MenuFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, menuFragment);
        Bundle args = new Bundle();
        args.putSerializable("Account",account);
        args.putSerializable("Bitter",bitter);
        menuFragment.setArguments(args);
        fragmentTransaction.commit();
    }

    /**
     * To facilitate the Android implementation's Back button.
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Activity lifecycle method. onPause() is called when this activity is being leaved. This time
     * will be used to save Bitter.
     */
    @Override
    public void onPause(){
        super.onPause();
        //Log.d(TAG, "onPause");
        bitter.save(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showUpButton() { getSupportActionBar().setDisplayHomeAsUpEnabled(true); }

    public void hideUpButton() { getSupportActionBar().setDisplayHomeAsUpEnabled(false); }
}
