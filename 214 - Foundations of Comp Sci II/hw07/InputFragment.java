package com.example.kbcalculator.fragments;

/**
 * Johnny So
 * 111158276
 * Homework #7
 * CSE 214 Recitation 12
 * Recitation TA: Charles Chen
 * Grading TA: Timothy Zhang
 * @author Johnny
 */

/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

// Java Imports
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// Project Imports
import com.example.kbcalculator.activities.*;
import com.example.kbcalculator.R;
import com.example.kbcalculator.adapters.SpinnerAdapters;
import com.example.kbcalculator.source.*;
import com.example.kbcalculator.interfaces.AsyncResponse;

// External Library
import big.data.DataInstantiationException;

/* ----------------------------- CLASS DEF ----------------------------- */

public class InputFragment extends Fragment implements AsyncResponse {


    /**
     * A DownloadMovieTask AsyncTask used to import movies from the API.
     */
    private class DownloadMovieTask extends AsyncTask<String, Void, Movie> {

        private AsyncResponse delegate = null;
        private ProgressDialog pDialog = null;


        public DownloadMovieTask(AsyncResponse delegate, ProgressDialog pDialog){
            this.delegate = delegate;
            this.pDialog = pDialog;
        }

        protected Movie doInBackground(String... movieTitle) {
            try {
                Movie movie = new Movie(movieTitle[0]);
                return movie;
            } catch(Exception e){
                return null;
            }
        }

        protected void onPostExecute(Movie result) {
            progressDialog.dismiss();
            delegate.processFinish(result);
        }
    }


    /* ----------------------- VARIABLES ----------------------- */

    // LOGIC & DATA VARS
    private ActorGraph actorGraph;
    private SpinnerAdapters.ActorAdapter actorAdapter;
    private String which;
    public static final String MOVIES = "movies";
    public static final String ACTORS = "actors";

    // FRAGMENT VARS
    private FragmentManager fragmentManager;

    // DISPLAY VARS
    private EditText inputBox;
    private Spinner menuSpinner;
    private Spinner menuSpinner2;
    private Button menuButton;
    private TextView errorMessage;
    private ProgressDialog progressDialog;

    /* ----------------------- FRAGMENT LIFECYCLE METHODS ----------------------- */

    /**
     * Required fragment lifecycle method to create the view.
     * @param inflater              the layout inflater
     * @param container             the ViewGroup container
     * @param savedInstanceState    the instance state of the app
     * @return                      the inflated view
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_input, container, false);

        // grab the display vars
        menuSpinner = (Spinner) inflatedView.findViewById(R.id.menu_spinner);
        menuSpinner2 = (Spinner) inflatedView.findViewById(R.id.menu_spinner2);
        menuButton = (Button) inflatedView.findViewById(R.id.menu_button);
        inputBox = (EditText) inflatedView.findViewById(R.id.input_box);
        errorMessage = (TextView) inflatedView.findViewById(R.id.error_message);

        // setup progressdialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.hide();
        progressDialog.setIndeterminate(true);

        // setup adapters
        actorAdapter = new SpinnerAdapters.ActorAdapter(getContext());

        // grab arguments
        which = getArguments().getString(MainActivity.WHICH);
        actorGraph = (ActorGraph) getArguments().getSerializable(MainActivity.AG);

        // set onclick listener
        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                handleInput(view,which);
            }
        });

        // set fragment vars
        fragmentManager = getFragmentManager();

        displayInput(which);

        return inflatedView;
    }

    /* ----------------------- HANDLING INPUT METHODS ----------------------- */

    /**
     * Sets up the input for the fragment.
     * @param which which option to display input for
     */
    public void displayInput(String which){
        List<Actor> allActors;
        switch (which){
            case MainActivity.IMPORT:
                // display input box
                inputBox.setHint("Enter the name of a movie");
                inputBox.setVisibility(View.VISIBLE);
                break;
            case MainActivity.PATH:
                allActors = actorGraph.getAllActors();
                if (allActors.size() > 0) {
                    // show actors
                    actorAdapter.setData(allActors);
                    menuSpinner.setAdapter(actorAdapter);
                    menuSpinner2.setAdapter(actorAdapter);
                    menuSpinner.setVisibility(View.VISIBLE);
                    menuSpinner2.setVisibility(View.VISIBLE);
                }
                else {
                    // display error message
                    menuButton.setVisibility(View.GONE);
                    showError("Sorry, no Actors have been imported yet.");
                }
                break;
            case MainActivity.BFS:
            case MainActivity.LOOKUP:
                allActors = actorGraph.getAllActors();
                if (allActors.size() > 0) {
                    // show actors
                    actorAdapter.setData(allActors);
                    menuSpinner.setAdapter(actorAdapter);
                    menuSpinner.setVisibility(View.VISIBLE);
                }
                else {
                    // display error
                    menuButton.setVisibility(View.GONE);
                    showError("Sorry, no Actors have been imported yet.");
                }
                break;
        }
    }

    /**
     * Handle the input entered by the user.
     * @param view  the view we are in
     * @param which which option the user selected from the main menu
     */
    public void handleInput(View view, String which){
        hideError();
        FragmentTransaction fragmentTransaction;
        DisplayFragment displayFragment;
        Bundle args;
        switch (which){
            case MainActivity.IMPORT:
                String movieTitle = grabInput(view);
                Movie movie;
                if (actorGraph.getMovie(movieTitle) != null){
                    showError("Sorry, the movie you entered has already been imported. Please try again.");
                }
                try {
                    progressDialog.setMessage("Importing Movie...");
                    progressDialog.show();
                    new DownloadMovieTask(this,progressDialog).execute(movieTitle);
                } catch(DataInstantiationException e){
                    showError("Sorry, the movie title you entered does not exist. Please try again.");
                }
                break;
            case MainActivity.PATH:
                Actor actor1 = grabSpinnerInput(view,1);
                Actor actor2 = grabSpinnerInput(view,2);
                ActorGraph.bfs(actor1.getName());
                LinkedList<String> path = actor2.getPath();
                Actor[] pathActors = new Actor[path.size()];
                for (int i = 0; i < path.size(); i++){
                    pathActors[i] = actorGraph.getActor(path.get(i));
                }
                fragmentTransaction = fragmentManager.beginTransaction();
                args = new Bundle();
                args.putString(MainActivity.WHICH,MainActivity.PATH);
                args.putSerializable(ACTORS,pathActors);
                displayFragment = new DisplayFragment();
                displayFragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container,displayFragment).commit();
                break;
            case MainActivity.BFS:
                Actor actor = grabSpinnerInput(view,1);
                LinkedList<String> bfs = ActorGraph.bfs(actor.getName());
                Actor[] bfsActors = new Actor[bfs.size()];
                for (int i = 0; i < bfs.size(); i++){
                    bfsActors[i] = actorGraph.getActor(bfs.get(i));
                }
                fragmentTransaction = fragmentManager.beginTransaction();
                args = new Bundle();
                args.putString(MainActivity.WHICH,MainActivity.BFS);
                args.putSerializable(ACTORS,bfsActors);
                displayFragment = new DisplayFragment();
                displayFragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container,displayFragment).commit();
                break;
            case MainActivity.LOOKUP:
                Actor actorLookup = grabSpinnerInput(view, 1);
                Actor[] actors = new Actor[1];
                actors[0] = actorLookup;
                fragmentTransaction = fragmentManager.beginTransaction();
                args = new Bundle();
                args.putString(MainActivity.WHICH,MainActivity.LOOKUP);
                args.putSerializable(ACTORS,actors);
                displayFragment = new DisplayFragment();
                displayFragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragment_container,displayFragment).commit();
                break;

        }
    }

    /**
     * Called by the receiver from the AsyncResponse interface method.
     * @param movie the movie that was imported
     */
    private void finishImportingMovie(Movie movie){
        try {
            actorGraph.addMovie(movie);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle args = new Bundle();
            args.putString(MainActivity.WHICH, MainActivity.IMPORT);
            Movie[] movies = new Movie[1];
            movies[0] = movie;
            args.putSerializable(MOVIES, movies);
            DisplayFragment displayFragment = new DisplayFragment();
            displayFragment.setArguments(args);
            fragmentTransaction.replace(R.id.fragment_container, displayFragment).commit();
        } catch (IllegalArgumentException e){
            showError("Sorry, but the specified Movie has already been added.");
        } catch(NullPointerException e){
            showError("Sorry, but the specified Movie does not exist.");
        }
    }

    /* ----------------------- GETTING ASYNCRESPONSE METHODS ----------------------- */

    /**
     * Receives the result from the AsyncTask DownloadMovieTask
     * @param output the Movie downloaded
     */
    @Override
    public void processFinish(Movie output){
        finishImportingMovie(output);
    }

    /* ----------------------- GRABBING INPUT METHODS ----------------------- */

    /**
     * Grabs the input text.
     * @param view the view we are in
     * @return     the input text
     */
    public String grabInput(View view){
        return inputBox.getText().toString();
    }

    /**
     * Grabs the selected Actor from the Spinner.
     * @param view  the view we are in
     * @param which which Spinner to grab
     * @return      the selected Actor
     */
    public Actor grabSpinnerInput(View view, int which){
        int position = 0;
        Actor actor = (Actor) menuSpinner.getItemAtPosition(position);
        switch (which){
            case 1:
                position = menuSpinner.getSelectedItemPosition();
                actor = (Actor) menuSpinner.getItemAtPosition(position);
                break;
            case 2:
                position = menuSpinner2.getSelectedItemPosition();
                actor = (Actor) menuSpinner2.getItemAtPosition(position);
                break;
        }
        return actor;
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

}
