package com.example.bitterplatform.fragments;

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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

// Project Imports
import com.example.bitterplatform.R;
import com.example.bitterplatform.activities.HomeActivity;
import com.example.bitterplatform.custom.ListAdapter;
import com.example.bitterplatform.custom.SpinnerAdapter;
import com.example.bitterplatform.source.Account;
import com.example.bitterplatform.source.Bitter;
import com.example.bitterplatform.source.User;



/* ----------------------------- CLASS DEF ----------------------------- */

public class DisplayFragment extends Fragment {

    /* ----------------------- VARIABLES ----------------------- */

    // LOGIC & DATA VARS
    public static final String DISPLAY_WHICH = "DISPLAY";
    public static final int DISPLAY_LIST = 2;
    public static final int DISPLAY_SPINNER = 3;
    private ListAdapter listAdapter;
    private SpinnerAdapter spinnerAdapter;

    private User[] users;
    private String flag;
    private Bitter bitter;
    private Account account;

    // DISPLAY VARS
    private ListView listView;
    private Spinner spinner;
    private Button selectButton;
    private TextView textView;

    // ONCLICK LISTENERS
    private View.OnClickListener followClickListener;
    private View.OnClickListener unfollowClickListener;

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
        View inflatedView = inflater.inflate(R.layout.fragment_display, container, false);

        // grab the display vars
        listView = (ListView) inflatedView.findViewById(R.id.listview);
        spinner = (Spinner) inflatedView.findViewById(R.id.spinner);
        selectButton = (Button) inflatedView.findViewById(R.id.button_display_select);
        textView = (TextView) inflatedView.findViewById(R.id.display_message);

        // initialize the adapters
        listAdapter = new ListAdapter(getContext());
        listView.setAdapter(listAdapter);
        spinnerAdapter = new SpinnerAdapter(getContext());
        spinner.setAdapter(spinnerAdapter);

        // grab the data passed
        int which = getArguments().getInt(DISPLAY_WHICH);
        users = (User[]) getArguments().getSerializable("Users");
        bitter = (Bitter) getArguments().getSerializable("Bitter");
        account = (Account) getArguments().getSerializable("Account");
        flag = getArguments().getString("flag");

        // initialize the on click listeners
        followClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                follow(view);
            }
        };

        unfollowClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                unfollow(view);
            }
        };

        display(which,users);

        return inflatedView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("ay");
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println();
                getActivity().onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }


    /* ----------------------- DISPLAY METHODS ----------------------- */

    /**
     * Main method that handles displaying what is needed.
     * @param whichType flag to determine whether to display data in a listview or a spinner
     * @param users     the data to display
     */
    public void display(int whichType, User[] users){
        // if no option here, hide everything & display a msg
        if (users.length == 0){
            textView.setText(getString(R.string.none));
            textView.setVisibility(View.VISIBLE);
        }
        else {
            // hide main message
            textView.setVisibility(View.GONE);
            switch (whichType) {
                case DISPLAY_LIST:
                    // easy here, just change data for the listAdapter and notify it has been changed
                    listAdapter.setData(users);
                    listAdapter.notifyDataSetChanged();
                    displayList();
                    hideSpinner();
                    break;
                case DISPLAY_SPINNER:
                    // easy here with custom implementation
                    spinnerAdapter = new SpinnerAdapter(getContext(), users);
                    spinner.setAdapter(spinnerAdapter);
                    displaySpinner();
                    hideList();
                    setButtonListener();
                    displayButton();
                    break;
                default:
                    hideList();
                    hideSpinner();
                    break;
            }
        }
    }

    /**
     * Makes the ListView visible.
     */
    public void displayList(){
        listView.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the ListView.
     */
    public void hideList(){
        listView.setVisibility(View.GONE);
    }

    /**
     * Displays the Spinner.
     */
    public void displaySpinner(){
        spinner.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the Spinner.
     */
    public void hideSpinner(){
        spinner.setVisibility(View.GONE);
    }

    /**
     * Displays the Button.
     */
    public void displayButton() { selectButton.setVisibility(View.VISIBLE); }

    /**
     * Hides the Button.
     */
    public void hideButton() { selectButton.setVisibility(View.GONE); }

    /* ----------------------- HANDLING INPUT METHODS ----------------------- */

    /**
     * Sets the button listener to the on click listener according to flag.
     */
    public void setButtonListener(){
        switch (flag){
            case MenuFragment.FOLLOW_SOMEONE:
                selectButton.setOnClickListener(followClickListener);
                selectButton.setText(getString(R.string.follow));
                break;
            case MenuFragment.UNFOLLOW_SOMEONE:
                selectButton.setOnClickListener(unfollowClickListener);
                selectButton.setText(getString(R.string.unfollow));
                break;
        }
    }

    /**
     * Method that handles following different users.
     * @param view  the view we are in
     */
    private void follow(View view){
        User following = grabSpinnerInput(view);
        bitter.addFollowing(account,following);
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * Method that handles unfollowing different users.
     * @param view  the view we are in
     */
    private void unfollow(View view){
        User following = grabSpinnerInput(view);
        bitter.removeFollowing(account,following);
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * Grabs the selected User from the Spinner.
     * @param view  the view we are in
     * @return      the selected User
     */
    public User grabSpinnerInput(View view){
        int position = spinner.getSelectedItemPosition();
        User user = (User) spinner.getItemAtPosition(position);
        return user;
    }


}
