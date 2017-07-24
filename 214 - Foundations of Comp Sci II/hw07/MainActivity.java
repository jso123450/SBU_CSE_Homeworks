package com.example.kbcalculator.activities;


/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

// Java Imports
import java.util.List;

// Project Imports
import com.example.kbcalculator.R;
import com.example.kbcalculator.source.*;
import com.example.kbcalculator.fragments.*;

/* ----------------------------- CLASS DEF ----------------------------- */

public class MainActivity extends AppCompatActivity {

    /* ----------------------- VARIABLES ----------------------- */

    // LOGIC & DATA VARS
    private ActorGraph actorGraph;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    public static final String AG = "AG";
    public static final String WHICH = "which";
    public static final String IMPORT = "I";
    public static final String ALL_ACTORS = "A";
    public static final String ALL_MOVIES = "M";
    public static final String PATH = "P";
    public static final String BFS = "B";
    public static final String LOOKUP = "L";
    public static final String QUIT = "Q";

    // DISPLAY VARS
    private Spinner menuSpinner;
    private Button menuButton;
    private TextView errorMessage;

    // ADAPTERS
    private ArrayAdapter spinnerAdapter;

    /* ----------------------- ACTIVITY LIFECYCLE METHODS ----------------------- */

    /**
     * Required activity lifecycle method. Performs things that should only happen once, in the
     * beginning of the activity.
     * @param savedInstanceState the instance state containing prev details about this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // calls the super oncreate and sets the content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // grab the display vars
        menuSpinner = (Spinner) findViewById(R.id.menu_spinner);
        menuButton = (Button) findViewById(R.id.menu_button);
        errorMessage = (TextView) findViewById(R.id.error_message);

        // initalize ActorGraph
        actorGraph = new ActorGraph();

        // setup the fragment vars
        fragmentManager = getSupportFragmentManager();

        // instantiate and attach the adapter
        spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.menu_select,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menuSpinner.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
    }


    /* ----------------------- HANDLING INPUT METHODS ----------------------- */

    /**
     * Handles menu input.
     * @param view the view we are in
     */
    public void handleMenuInput(View view){
        hideError();
        String input = grabSpinnerInput(view);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        Bundle args = new Bundle();
        args.putSerializable(AG,actorGraph);
        DisplayFragment displayFragment;
        InputFragment inputFragment;
        switch (input){
            case IMPORT:
                args.putString(WHICH,IMPORT);
                inputFragment = new InputFragment();
                inputFragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container,inputFragment).commit();
                break;
            case ALL_ACTORS:
                List<Actor> allActors = actorGraph.getAllActors();
                if (allActors.size() > 0) {
                    displayFragment = new DisplayFragment();
                    Actor[] allActorsArray = new Actor[allActors.size()];
                    allActorsArray = allActors.toArray(allActorsArray);
                    args.putString(WHICH, ALL_ACTORS);
                    args.putSerializable(InputFragment.ACTORS, allActorsArray);
                    displayFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.fragment_container, displayFragment).commit();
                }
                else {
                    showError("Sorry, but no Actors have been imported yet.");
                }
                break;
            case ALL_MOVIES:
                List<Movie> allMovies = actorGraph.getAllMovies();
                if (allMovies.size() > 0 ) {
                    displayFragment = new DisplayFragment();
                    Movie[] allMoviesArray = new Movie[allMovies.size()];
                    allMoviesArray = allMovies.toArray(allMoviesArray);
                    args.putString(WHICH, ALL_MOVIES);
                    args.putSerializable(InputFragment.MOVIES, allMoviesArray);
                    displayFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.fragment_container, displayFragment).commit();
                }
                else {
                    showError("Sorry, no Movies have been imported yet.");
                }
                break;
            case PATH:
                args.putString(WHICH,PATH);
                inputFragment = new InputFragment();
                inputFragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container,inputFragment).commit();
                break;
            case BFS:
                args.putString(WHICH,BFS);
                inputFragment = new InputFragment();
                inputFragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container,inputFragment).commit();
                break;
            case LOOKUP:
                args.putString(WHICH,LOOKUP);
                inputFragment = new InputFragment();
                inputFragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container,inputFragment).commit();
                break;
            case QUIT:
                args.putString(WHICH,QUIT);
                quit();
                break;
        }
    }

    /**
     * Grab the main menu Spinner selection.
     * @param view  the view we are in
     * @return      the selected flag as appropriate
     */
    public String grabSpinnerInput(View view){
        String input = menuSpinner.getSelectedItem().toString();
        return input.substring(0,1);
    }

     /* ----------------------- DISPLAY METHODS ----------------------- */

    /**
     * Shows an error message.
     * @param error the error message to show
     */
    public void showError(String error){
        errorMessage.setText(error);
        errorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the error message.
     */
    public void hideError(){
        errorMessage.setText("");
        errorMessage.setVisibility(View.GONE);
    }

    /**
     * Quits the app.
     */
    public void quit(){
        finish();
        System.exit(0);
    }






}
