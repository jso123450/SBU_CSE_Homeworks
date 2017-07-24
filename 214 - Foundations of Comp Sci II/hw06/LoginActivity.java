package com.example.bitterplatform.activities;

/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Java Imports
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


// Project Imports
import com.example.bitterplatform.R;
import com.example.bitterplatform.source.Bitter;

/* ----------------------------- CLASS DEF ----------------------------- */

public class LoginActivity extends AppCompatActivity {

    /* ----------------------- VARIABLES ----------------------- */

    // LOGIC & DATA VARS
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private Bitter bitter;

    // DISPLAY VARS
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView signupLink;
    private ProgressDialog progressDialog;


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
        setContentView(R.layout.activity_login);

        // load past Bitters if any
        File file = new File(this.getFilesDir(),Bitter.BITTER_FILE);
        if (file.exists()){
            try {
                FileInputStream fileIn = openFileInput(Bitter.BITTER_FILE);
                ObjectInputStream inStream = new ObjectInputStream(fileIn);
                bitter = (Bitter) inStream.readObject();
                inStream.close();
                System.out.println(bitter);
            } catch(Exception e){
                Log.e(TAG,"file reading",e);
            }
        }
        else
            bitter = new Bitter();

        // grab the display vars
        emailInput = (EditText) findViewById(R.id.input_email);
        passwordInput = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.button_login);
        signupLink = (TextView) findViewById(R.id.link_signup);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.logging_in));

        // set listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                login(view);
            }
        });

        // set listener for the sign up link textview
        signupLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // start the signup activity
                Intent signupIntent = new Intent(getApplicationContext(), SignupActivity.class);;
                signupIntent.putExtra("Bitter",bitter);
                startActivityForResult(signupIntent,REQUEST_SIGNUP);
            }
        });
    }

    /**
     * Grabs what is sent back from startActivityForResult.
     * @param requestCode   the request code
     * @param resultCode    the resultant code
     * @param data          any data that might be passed back
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_SIGNUP){
            if (resultCode == RESULT_OK){
                // on successful signup, update the bitter obj here
                bitter = (Bitter) data.getSerializableExtra("Bitter");
                //loginButton.setEnabled(true);
            }
        }
    }

    /**
     * If Back is pressed here, it should leave the app.
     */
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
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

    /* ----------------------- LOGIN METHODS ----------------------- */

    /**
     * Attempts to login the User to his Account.
     * @param view  the view we are in
     */
    public void login(View view){
        //Log.d(TAG, "Login");

        final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        int loginValue = bitter.login(email,password);

        switch (loginValue){
            case Bitter.EMAIL_NOT_FOUND:
                onLoginFailed(Bitter.EMAIL_NOT_FOUND);
                break;
            case Bitter.INVALID_PASSWORD:
                onLoginFailed(Bitter.INVALID_PASSWORD);
                break;
            case Bitter.VALID_LOGIN:
                progressDialog.show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                onLoginSuccess(email);
                            }
                        }, 3000);
                break;
            default:
                break;
        }

    }

    public void onLoginSuccess(String email){
        //Log.d(TAG,"onLoginSuccess");
        //loginButton.setEnabled(false);
        progressDialog.dismiss();

        // move to HomeActivity
        Intent loggedinIntent = new Intent(getApplicationContext(), HomeActivity.class);;
        loggedinIntent.putExtra("Bitter",bitter);
        loggedinIntent.putExtra("Email",email);
        startActivity(loggedinIntent);
    }

    /**
     * If login is unsuccessful, display a message.
     * @param flag  details about what went wrong with the login
     */
    public void onLoginFailed(int flag){
        progressDialog.hide();
        String message = "";
        switch (flag){
            case Bitter.EMAIL_NOT_FOUND:
                message+= "The email specified has not been registered.";
                break;
            case Bitter.INVALID_PASSWORD:
                message+= "The password entered is invalid.";
                break;
            default:
                message+= "Login Failed";
                break;
        }
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        //loginButton.setEnabled(true);
    }

    /**
     * Used to quit the application.
     * @param view  the view we are in
     */
    public void quit(View view){
        finish();
        System.exit(0);
    }




}
