package com.example.kbcalculator.fragments;

/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

// Java Imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Project Imports
import com.example.kbcalculator.activities.*;
import com.example.kbcalculator.R;
import com.example.kbcalculator.adapters.ListAdapters;
import com.example.kbcalculator.source.*;



/* ----------------------------- CLASS DEF ----------------------------- */

public class DisplayFragment extends Fragment {

    /* ----------------------- VARIABLES ----------------------- */

    // LOGIC & DATA VARS
    private ListAdapters.ActorAdapter actorAdapter;
    private ListAdapters.MovieAdapter movieAdapter;
    private ActorGraph actorGraph;
    private List<Actor> actors;
    private List<Movie> movies;

    // DISPLAY VARS
    private TextView textView;
    private ListView listView;

    /* ----------------------- FRAGMENT LIFECYCLE METHODS ----------------------- */

    /**
     * Fragment lifecycle method. Used to initialize data.
     * @param savedInstanceState (if any) previously saved instance state
     */
    public void onCreate(Bundle savedInstanceState){
        // calls the super oncreate
        super.onCreate(savedInstanceState);

        // initialize the adapters
        actorAdapter = new ListAdapters.ActorAdapter(getContext());
        movieAdapter = new ListAdapters.MovieAdapter(getContext());
    }

    /**
     * Required fragment lifecycle method to create the view.
     * @param inflater              the layout inflater
     * @param container             the ViewGroup container
     * @param savedInstanceState    the instance state of the app
     * @return                      the inflated view
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_display, container, false);

        // grab the display vars
        textView = (TextView) inflatedView.findViewById(R.id.display_message);
        listView = (ListView) inflatedView.findViewById(R.id.listview);

        // grab the data passed
        String which = getArguments().getString(MainActivity.WHICH);
        actorGraph = (ActorGraph) getArguments().getSerializable(MainActivity.AG);
        switch (which){
            case MainActivity.IMPORT:
                movies = new ArrayList<Movie>(Arrays.asList((Movie[]) getArguments().getSerializable(InputFragment.MOVIES)));
                Movie movie = movies.get(0);
                textView.setText(Html.fromHtml(movie.toString()));
                break;
            case MainActivity.ALL_MOVIES:
                movies = new ArrayList<Movie>(Arrays.asList((Movie[]) getArguments().getSerializable(InputFragment.MOVIES)));
                movieAdapter.setData(movies);
                listView.setAdapter(movieAdapter);
                movieAdapter.notifyDataSetChanged();
                break;
            case MainActivity.ALL_ACTORS:
            case MainActivity.PATH:
            case MainActivity.BFS:
                actors = new ArrayList<Actor>(Arrays.asList((Actor[]) getArguments().getSerializable(InputFragment.ACTORS)));
                actorAdapter.setData(actors);
                listView.setAdapter(actorAdapter);
                actorAdapter.notifyDataSetChanged();
                break;
            case MainActivity.LOOKUP:
                actors = new ArrayList<Actor>(Arrays.asList((Actor[]) getArguments().getSerializable(InputFragment.ACTORS)));
                Actor actor = actors.get(0);
                textView.setText(Html.fromHtml(actor.toString()));
                break;
        }
        display(which);
        return inflatedView;
    }

    /* ----------------------- DISPLAY METHODS ----------------------- */

    public void display(String which){
        switch (which){
            case MainActivity.IMPORT:
            case MainActivity.LOOKUP:
                listView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                break;
            case MainActivity.ALL_MOVIES:
            case MainActivity.ALL_ACTORS:
            case MainActivity.PATH:
            case MainActivity.BFS:
                textView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                break;
        }
    }

}
