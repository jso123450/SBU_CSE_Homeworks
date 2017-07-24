package com.example.tictactoeai;


/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannedString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

// Project Imports
import com.example.tictactoeai.custom.*;
import com.example.tictactoeai.source.*;


/* ----------------------------- CLASS DEF ----------------------------- */

public class MainActivity extends AppCompatActivity {


    /* -----------------------    VARS    ----------------------- */

    // Data Vars
    private GameTree tree;
    private Box PLAYERS_TURN = Box.X;
    private Box AI_TURN = Box.O;
    private boolean running = true;

    // UI & Display Vars
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner menuSpinner;
    private EditText inputBox1;
    private ExpandableTextView currentProbs;
    private ExpandableTextView hoverProbs;
    private TextView menuDisplay, errorDisplay;
    private Button enterButton, restartButton, quitButton;
    private static Handler handler;
    private ProgressBar progressBar;
    private GridView gridView;
    private GridAdapter gridAdapter;
    private View.OnClickListener startSetup;


    /* -----------------------    INIT     ----------------------- */

    /**
     * Required lifecycle function that will initialize all the related view variables and
     * setup the layout.
     * @param savedInstanceState current state of app.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // call super method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize UI & display vars
        menuSpinner = (Spinner) findViewById(R.id.menu_selection);
        inputBox1 = (EditText) findViewById(R.id.input1);
        currentProbs = (ExpandableTextView) findViewById(R.id.current_node_probs);
        hoverProbs = (ExpandableTextView) findViewById(R.id.hover_over_probs);
        menuDisplay = (TextView) findViewById(R.id.menu_display);
        errorDisplay = (TextView) findViewById(R.id.error_display);
        enterButton = (Button) findViewById(R.id.enter_button);
        restartButton = (Button) findViewById(R.id.restart_button);
        quitButton = (Button) findViewById(R.id.quit_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        gridView = (GridView) findViewById(R.id.gridview);


        // will be used many times to reset the enter button to main menu selection
        startSetup = new View.OnClickListener(){
            public void onClick(View view){
                startSetup(view);
            }
        };

        // initialize handler
        handler = new buildTreeHandler();
    }

    /**
     * Private Handler that will be used to check when the GameTree is done building. It is built
     * only once, before the first game is played.
     */
    private class buildTreeHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            if (msg.what == 0)
                setupGame();
            else
                startSetup();
        }
    }

    /* -----------------------   GAME LOGIC AND SETUP   ----------------------- */

    /**
     * Wrapper for startSetup()
     * @param view the view we are in
     */
    public void startSetup(View view){
        startSetup();
    }

    /**
     * The starting logic that will run the game. It initializes display elements to welcome.
     */
    private void startSetup(){
        // make the spinner selection visible
        menuSpinner.setVisibility(View.VISIBLE);

        // set menu display text
        setMenuDisplay(getString(R.string.select_type));

        // set click listener for the button
        View.OnClickListener takePlayerTypeListener = new View.OnClickListener(){
            public void onClick(View view){
                grabPlayerTypeListener(view);
            }
        };
        setButtonText(getString(R.string.next));
        enterButton.setOnClickListener(takePlayerTypeListener);
        enterButton.setVisibility(View.VISIBLE);

        running = true;
    }

    /**
     * Grabs the desired player type, (Box.X or Box.O) and calls the GridView setup. No need to
     * worry about bad input here, because the only available input is from a Spinner.
     * @param view the view we are in
     */
    private void grabPlayerTypeListener(View view){
        String desiredType = grabSpinnerSelection(view);
        resetSpinners();
        Box type = Box.X;
        if (desiredType.equals("O"))
            type = Box.O;
        PLAYERS_TURN = type;
        AI_TURN = ((PLAYERS_TURN == Box.X)? Box.O : Box.X);

        if (tree == null) {
            // built the first time a player enters the app

            setMenuDisplay("Loading...");
            enterButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            // run heavy load on a different worker thread
            Thread buildingTrees = new Thread(new Runnable(){
                public void run(){
                    // initialize the GameTree
                    tree = new GameTree(Box.X);
                    try {
                        GameTree.buildTree(tree.getRoot(), Box.X);
                    } catch (IllegalMoveException ime) {
                        // should never even be thrown here
                    }
                    // lets the handler know the tree is done building
                    handler.sendEmptyMessage(0);
                }
            });
            buildingTrees.start();
        }
        else
            setupGame();    // if already built, use same tree

    }

    /**
     * Initialize the grid and other display elements to setup for the game.
     */
    private void setupGame(){

        // make the restart button visible
        restartButton.setVisibility(View.VISIBLE);


        progressBar.setVisibility(View.GONE);
        updateProbs();

        // set menu display to prompt player move
        setMenuDisplay(getString(R.string.make_your_move));

        // setup gridview
        if (gridAdapter == null) {
            gridAdapter = new GridAdapter(this, tree.getCursor(), PLAYERS_TURN);
            gridView.setAdapter(gridAdapter);
        }
        else if (gridAdapter.getPlayersTurn() != PLAYERS_TURN) {
            gridAdapter.setPlayersTurn(PLAYERS_TURN);
            gridAdapter.notifyDataSetChanged();
        }

        // set visibility to true
        restartButton.setVisibility(View.VISIBLE);
        currentProbs.setVisibility(View.VISIBLE);
        hoverProbs.setVisibility(View.VISIBLE);

        // set item click listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
           public void onItemClick(AdapterView<?> parent, View view, int position, long id){
               grabPlayerMove(parent,view,position,id);
           }
        });

        // set item longclick listener
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                displayConfigStats(parent,view,position,id);
                return true;
            }
        });

        // make the gridview visible
        gridView.setVisibility(View.VISIBLE);

        // change button mode
        enterButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                undo(view);
            }
        });
        enterButton.setText(getString(R.string.undo));
        enterButton.setVisibility(View.VISIBLE);
    }

    /**
     * Undoes the last move(s) accordingly so that it will be the player's move again. Will not do
     * anything if the tree cursor is at the node.
     * @param view  the view we are in
     */
    private void undo(View view){
        if (tree.getCursor().getIsEnd()) {
            running = true;
            // set item click listener
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    grabPlayerMove(parent,view,position,id);
                }
            });
        }
        if (tree.getCursor().getIsEnd() && tree.getCursor().getCurrentTurn() == AI_TURN) {
            tree.undo();
            updateGrid();
            hideError();
        }
        else if (tree.getCursor() != tree.getRoot()) {
            tree.undo();
            tree.undo();
            updateGrid();
            hideError();
        }
        else
            showError("Sorry, there are no moves to undo.");
    }

    /**
     * The method that is attached to the GridView items to update the tree cursor and display.
     * @param parent    the parent adapterview
     * @param view      the view clicked
     * @param position  the position in the data clicked
     * @param id        the row id of the view clicked
     */
    private void grabPlayerMove(AdapterView<?> parent, View view, int position, long id){
        if (running) {
            try {
                tree.makeMove(position);
                tree.AIPlayGame();
                updateGrid();
                updateProbs();
                hideError();
                if (tree.getCursor().getIsEnd()) {
                    endScreen();
                }
            } catch (IllegalMoveException ime) {
                showError(getString(R.string.illegal_move));
            }
        }
    }

    /**
     * Displays end of the game information. Quit button is accessible here.
     */
    public void endScreen(){
        gridView.setOnItemClickListener(null);
        gridView.setClickable(false);
        running = false;
        boolean AIWon = (tree.getCursor().getWinner() == Box.O);
        if (AIWon)
            setMenuDisplay("You have lost the match.");
        else
            setMenuDisplay("The match ended in a tie.");
        enterButton.setVisibility(View.GONE);
        quitButton.setVisibility(View.VISIBLE);
    }


    /* -----------------------   HANDLING USER INPUT METHODS   ----------------------- */


    /**
     * Grabs the selected Spinner item.
     * @param  view  the view we are in
     * @return       the selected Spinner item
     */
    private String grabSpinnerSelection(View view){
        String input = menuSpinner.getSelectedItem().toString();
        return input;
    }

    /* -----------------------   DISPLAY CONTROL METHODS   ----------------------- */

    /**
     * Updates the GridView data and display.
     */
    private void updateGrid(){
        gridAdapter.setData(tree.getCursor());
        gridAdapter.notifyDataSetChanged();
    }

    /**
     * Updates the current win, draw, and lose probabilities of the game.
     */
    private void updateProbs(){
        if (tree != null) {
            if (tree.getCursor() != tree.getRoot()) {
                String currentProbText = "<b>" + getString(R.string.current_node_probs) + "</b>";
                currentProbText+= "<br>";
                currentProbText+= "Winning Prob. : " + (Math.round(tree.getCursor().getWinProb() * 100.0) / 100.0);
                currentProbText+= "<br>";
                currentProbText+= "Draw Prob. : " + (Math.round(tree.getCursor().getDrawProb() * 100.0) / 100.0);
                currentProbText+= "<br>";
                currentProbText+= "Losing Prob. : " + (Math.round(tree.getCursor().getLoseProb() * 100.0) / 100.0);
                currentProbs.setText(Html.fromHtml(currentProbText));
            }
            else {
                String currentProbText = "<b>" + getString(R.string.current_node_probs) + "</b>";
                currentProbText+= "<br>";
                currentProbText+= "Sorry, no probabilities to display here :). Do your best!";
                currentProbs.setText(Html.fromHtml(currentProbText));
            }
        }
    }

    /**
     * Updates the win, draw, and lose properties of the position that was long clicked.
     * @param parent    the adapterview parent
     * @param view      the view clicked
     * @param position  the position of the view clicked
     * @param id        the row id of the view clicked
     */
    private void displayConfigStats(AdapterView<?> parent, View view, int position, long id){
        if (running){
            if (tree.getCursor() != tree.getRoot() && tree.getCursor().getConfig()[position] != null) {
                String hoverProbText = "<b>" + getString(R.string.long_press_probs) + "</b>";
                hoverProbText += "<br>";
                hoverProbText += "Winning Prob. : " + (Math.round(tree.getCursor().getConfig()[position].getWinProb() * 100.0) / 100.0);
                hoverProbText += "<br>";
                hoverProbText += "Draw Prob. : " + (Math.round(tree.getCursor().getConfig()[position].getDrawProb() * 100.0) / 100.0);
                hoverProbText += "<br>";
                hoverProbText += "Losing Prob. : " + (Math.round(tree.getCursor().getConfig()[position].getLoseProb() * 100.0) / 100.0);
                hoverProbs.setText(Html.fromHtml(hoverProbText));
            }
            else {
                String hoverProbText = "<b>" + getString(R.string.long_press_probs) + "</b>";
                hoverProbText+= "<br>";
                hoverProbText+= "Sorry, but there are no probabilities to check here. Do your best! :)";
                hoverProbs.setText(Html.fromHtml(hoverProbText));
            }
        }
    }

    /**
     * Reset to initial welcome screen.
     * @param view  the view we are in
     */
    public void resetToWelcome(View view){
        // hide error messages
        hideError();

        // hide spinner
        resetSpinners();

        // reset menu screen
        resetMenuDisplay();

        // reset buttons
        resetButtons();

        // reset the ExpandableTextViews
        resetStatusDisplay();

        // hide gridview
        tree.setCursor(tree.getRoot());
        updateGrid();
        gridView.setVisibility(View.GONE);

        // restart game logic
        startSetup(view);
    }

    /**
     * Displays an error that occurred.
     * @param error the error message to be displayed.
     */
    public void showError(String error){
        errorDisplay.setText(error);
        errorDisplay.setVisibility(View.VISIBLE);
    }



    /**
     * Hides the error after another input is made.
     */
    public void hideError(){
        errorDisplay.setText("");
        errorDisplay.setVisibility(View.GONE);
    }

    /**
     * Reset the spinners to initial state.
     */
    private void resetSpinners(){
        menuSpinner.setVisibility(View.GONE);
    }

    /**
     * Sets the menu display to the given string.
     * @param menu the message to be displayed
     */
    private void setMenuDisplay(String menu){
        SpannedString menuSpanned = new SpannedString(Html.fromHtml(menu));
        menuDisplay.setText(menuSpanned);
    }

    /**
     * Resets the menu display.
     */
    private void resetMenuDisplay(){
        setMenuDisplay(getString(R.string.welcome));
    }

    /**
     * Reset the button to initial welcome screen.
     */
    private void resetButtons(){
        setButtonText(getString(R.string.begin));
        enterButton.setOnClickListener(startSetup);
        if (restartButton.getVisibility() != View.GONE)
            restartButton.setVisibility(View.GONE);
        if (quitButton.getVisibility() != View.GONE)
            quitButton.setVisibility(View.GONE);
    }

    /**
     * Set the button text to desired String.
     * @param text String that the button text will be set to
     */
    private void setButtonText(String text){
        enterButton.setText(text);
    }

    /**
     * Hides the ExpandableTextViews.
     */
    private void resetStatusDisplay(){
        if (currentProbs.getVisibility() != View.GONE)
            currentProbs.setVisibility(View.GONE);
        if (hoverProbs.getVisibility() != View.GONE)
            hoverProbs.setVisibility(View.GONE);
    }

    /**
     * Smoothly exits the application.
     */
    public void quit(View view){
        finish();
        System.exit(0);
    }

}
