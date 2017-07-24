package com.example.bitterplatform.fragments;

/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

// Java Imports
import java.util.ArrayList;
import java.util.Arrays;


// Project Imports
import com.example.bitterplatform.R;
import com.example.bitterplatform.activities.LoginActivity;
import com.example.bitterplatform.source.Account;
import com.example.bitterplatform.source.Bitter;
import com.example.bitterplatform.source.User;


/* ----------------------------- CLASS DEF ----------------------------- */

public class MenuFragment extends Fragment {

    /* ----------------------- VARIABLES ----------------------- */

    // LOGIC & DATA VARS
    public static final String FOLLOW_SOMEONE = "F";
    public static final String UNFOLLOW_SOMEONE = "U";
    public static final String VIEW_FOLLOWERS = "V";
    public static final String SEE_FOLLOWING = "S";
    public static final String LIST_ALL_USERS = "A";
    public static final String LOGOUT = "L";
    public static final String CLOSE_ACC = "C";
    private Bitter bitter;
    private Account account;

    // DISPLAY VARS
    private Spinner menuSpinner;
    private Button menuButton;
    private ProgressDialog progressDialog;

    // FRAGMENT VARS
    private FragmentManager fragmentManager;
    private DisplayFragment displayFragment;

    /* ----------------------- FRAGMENT LIFECYCLE METHODS ----------------------- */
    /**
     * Required lifecycle method to create the view.
     * @param inflater              the layout inflater
     * @param container             the ViewGroup container
     * @param savedInstanceState    the instance state of the app
     * @return                      the inflated view
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_menu, container, false);

        // grab display vars
        menuSpinner = (Spinner) inflatedView.findViewById(R.id.menu_spinner);
        menuButton = (Button) inflatedView.findViewById(R.id.button_menu);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);

        // fragment vars
        fragmentManager = getFragmentManager();
        displayFragment = new DisplayFragment();

        // grab data vars
        Bundle args = getArguments();
        bitter = (Bitter) args.getSerializable("Bitter");
        account = (Account) args.getSerializable("Account");

        // instantiate menuSpinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),R.array.menu_options,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menuSpinner.setAdapter(spinnerAdapter);

        // add onclick listener to the button
        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                display(view);
            }
        });

        return inflatedView;
    }


    /* ----------------------- DISPLAYING INPUT METHODS ----------------------- */

    /**
     * Main method that handles input from the menu.
     * @param view  the view we are in
     */
    public void display(View view){
        String which = grabSpinnerInput(view);
        switch (which){
            case LOGOUT:
                // if logout, move back to LoginActivity & return
                progressDialog.setMessage(getString(R.string.logging_out));
                progressDialog.show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                logout();
                            }
                        }, 3000);
                return;
            case CLOSE_ACC:
                // if closing account, close account and move back to LoginActivity
                progressDialog.setMessage(getString(R.string.deleting_account));
                progressDialog.show();
                try {
                    bitter.removeUser(bitter.getAccountEmail(account));
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    logout();
                                }
                            }, 3000);
                } catch(Exception e){
                    progressDialog.dismiss();
                    return;
                }
                return;
        }

        // else, setup to change fragments
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        ArrayList<User> following;
        ArrayList<User> followers;
        User[] users = new User[0];
        Bundle args = new Bundle();
        args.putSerializable("Bitter",bitter);
        args.putSerializable("Account",account);
        args.putString("flag",which);
        switch (which){
            case FOLLOW_SOMEONE:
                // if following someone, get the users that are not being followed & not the user
                following = account.getFollowing();
                ArrayList<User> allUsers = new ArrayList<User>(Arrays.asList(bitter.getAllUsers()));
                allUsers.removeAll(following);
                ArrayList<User> self = new ArrayList<User>();
                self.add(bitter.getUser(bitter.getAccountEmail(account)));
                allUsers.removeAll(self);
                users = new User[allUsers.size()];
                allUsers.toArray(users);
                args.putSerializable("Users",users);
                args.putInt(DisplayFragment.DISPLAY_WHICH,DisplayFragment.DISPLAY_SPINNER);
                break;
            case UNFOLLOW_SOMEONE:
                // if unfollowing, get the list of currently followed users
                following = account.getFollowing();
                users = new User[following.size()];
                following.toArray(users);
                args.putSerializable("Users",users);
                args.putInt(DisplayFragment.DISPLAY_WHICH,DisplayFragment.DISPLAY_SPINNER);
                break;
            case VIEW_FOLLOWERS:
                // get the list of currently followed users
                followers = account.getFollowers();
                users = new User[followers.size()];
                followers.toArray(users);
                args.putSerializable("Users",users);
                args.putInt(DisplayFragment.DISPLAY_WHICH,DisplayFragment.DISPLAY_LIST);
                break;
            case SEE_FOLLOWING:
                // get the list of users the user is following
                following = account.getFollowing();
                users = new User[following.size()];
                following.toArray(users);
                args.putSerializable("Users",users);
                args.putInt(DisplayFragment.DISPLAY_WHICH,DisplayFragment.DISPLAY_LIST);
                break;
            case LIST_ALL_USERS:
                // get all users
                users = bitter.getAllUsers();
                args.putSerializable("Users",users);
                args.putInt(DisplayFragment.DISPLAY_WHICH,DisplayFragment.DISPLAY_LIST);
                break;
            default:
                // should never happen
                users = bitter.getAllUsers();
                args.putSerializable("Users",users);
                args.putInt(DisplayFragment.DISPLAY_WHICH,DisplayFragment.DISPLAY_LIST);
                break;
        }
        // set the arguments
        displayFragment.setArguments(args);
        // begin the transaction
        fragmentTransaction.replace(R.id.fragment_container,displayFragment).commit();
    }

    /**
     * Method that handles logging out (a.k.a. moving back to Login screen)
     */
    public void logout(){
        progressDialog.dismiss();
        Intent intent = new Intent(getContext(),LoginActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    /* ----------------------- GRABBING INPUT METHODS ----------------------- */

    /**
     * Grab the main menu Spinner selection.
     * @param view  the view we are in
     * @return      the selected flag as appropriate
     */
    public String grabSpinnerInput(View view){
        String input = menuSpinner.getSelectedItem().toString();
        switch (input){
            case "Follow somebody.":
                return FOLLOW_SOMEONE;
            case "Unfollow somebody.":
                return UNFOLLOW_SOMEONE;
            case "View followers.":
                return VIEW_FOLLOWERS;
            case "See who you follow.":
                return SEE_FOLLOWING;
            case "List all users.":
                return LIST_ALL_USERS;
            case "Logout.":
                return LOGOUT;
            case "Close your account.":
                return CLOSE_ACC;
            default:
                return LIST_ALL_USERS;
        }
    }

}
