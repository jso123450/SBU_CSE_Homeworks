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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bitterplatform.R;
import com.example.bitterplatform.source.Account;
import com.example.bitterplatform.source.Bitter;
import com.example.bitterplatform.source.Email;
import com.example.bitterplatform.source.Password;
import com.example.bitterplatform.source.User;

/* ----------------------------- CLASS DEF ----------------------------- */

public class SignupActivity extends AppCompatActivity{

    /* ----------------------- VARIABLES ----------------------- */

    // LOGIC & DATA VARSS
    private static final String TAG = "SignupActivity";
    private static final int ILLEGAL_EMAIL = 1;
    private static final int ILLEGAL_PASSWORD = 2;
    private static final int EMAIL_IN_USE = 3;
    private static Bitter bitter;

    // DISPLAY VARS
    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private Button signupButton;
    private TextView loginLink;
    private ProgressDialog progressDialog;



    /* ----------------------- ACTIVITY LIFECYCLE METHODS ----------------------- */

    /**
     * Required activity lifecycle method. Perform things once, upon creation of this activity.
     * @param savedInstanceState    previously saved instance state, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        // sets the layout and calls the super's onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // grab the Bitter object passed through the intent
        Intent intent = getIntent();
        bitter = (Bitter) intent.getSerializableExtra("Bitter");

        // grab the display vars
        nameInput = (EditText) findViewById(R.id.input_name);
        emailInput = (EditText) findViewById(R.id.input_email);
        passwordInput = (EditText) findViewById(R.id.input_password);
        signupButton = (Button) findViewById(R.id.button_signup);
        loginLink = (TextView) findViewById(R.id.link_login);
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.creating_account));

        // set the click listeners
        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // head back to login screen
                progressDialog.dismiss();
                finish();
            }
        });

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


    /* ----------------------- SIGNUP METHODS ----------------------- */

    /**
     * Main method that handles the logic for registering a new account.
     */
    public void signup(){
        //Log.d(TAG,"Signup");
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        try {
            // attempt to validate Email and Password; if invalid, it will be caught in the catch block
            final Email emailWrapper = new Email(email);
            final Password passwordWrapper = new Password(password);
            final User user = new User(name,emailWrapper);
            final Account account = new Account(name,passwordWrapper);

            progressDialog.show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            try {
                                bitter.addUser(emailWrapper, user, account);
                                onSignupSuccess();
                            } catch (IllegalArgumentException iae) {
                                String message = iae.getMessage();
                                switch (message) {
                                    case "Invalid Email":
                                        onSignupFailed(ILLEGAL_EMAIL);
                                        break;
                                    case "Invalid Password":
                                        onSignupFailed(ILLEGAL_PASSWORD);
                                        break;
                                    case "Taken Email":
                                        onSignupFailed(EMAIL_IN_USE);
                                        break;
                                    default:
                                        // should never happen
                                        onSignupFailed(ILLEGAL_EMAIL);
                                        break;
                                }
                                progressDialog.dismiss();
                            }
                        }
                    }, 3000);

        } catch(IllegalArgumentException iae){
            String message = iae.getMessage();
            switch (message){
                case "Invalid Email":
                    onSignupFailed(ILLEGAL_EMAIL);
                    break;
                case "Invalid Password":
                    onSignupFailed(ILLEGAL_PASSWORD);
                    break;
                case "Taken Email":
                    onSignupFailed(EMAIL_IN_USE);
                    break;
                default:
                    // should never happen
                    onSignupFailed(ILLEGAL_EMAIL);
                    break;
            }
        }
    }

    /**
     * Upon successful registration, finish this activity and move back to login.
     */
    public void onSignupSuccess(){
        //Log.d(TAG, "onSignupSuccess");
        progressDialog.dismiss();
        //signupButton.setEnabled(false);
        Intent data = new Intent();
        data.putExtra("Bitter",bitter);
        setResult(RESULT_OK,data);
        finish();
    }

    /**
     * If registering an account failed, display a message why.
     * @param flag details about why the registration failed
     */
    public void onSignupFailed(int flag){
        progressDialog.hide();
        if (flag == ILLEGAL_EMAIL) {
            Toast.makeText(getBaseContext(), "Sorry, your email is invalid", Toast.LENGTH_LONG).show();
        }
        if (flag == ILLEGAL_PASSWORD){
            String message = "Sorry, your password is invalid. It must have " + getString(R.string.password_req) + ".";
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        }
        if (flag == EMAIL_IN_USE){
            Toast.makeText(getBaseContext(), "Sorry, your email is already in use.", Toast.LENGTH_LONG).show();
        }
        //signupButton.setEnabled(true);
    }



}
