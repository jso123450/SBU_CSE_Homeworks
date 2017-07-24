package com.example.tictactoeai.fragments;


/* ---------------------------------------- IMPORTS ---------------------------------------- */

// Android Imports
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannedString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Project Imports
import com.example.tictactoeai.R;



/* ---------------------------------------- CLASS DEF. ---------------------------------------- */


public class MenuFragment extends Fragment {

    /* ----------------------- VARS ----------------------- */

    private TextView display;

    /* ----------------------- INITIALIZATION ----------------------- */

    /**
     * Required lifecycle method.
     * @param inflater              the layout inflater to inflate the layout
     * @param container             the ViewGroup container
     * @param savedInstanceState    the current state of the app
     * @return                      the inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View inflatedView = inflater.inflate(R.layout.fragment_menu, container, false);
        display = (TextView)inflatedView.findViewById(R.id.menu_display);
        setText(getString(R.string.welcome));
        return inflatedView;
    }


    /**
     * Sets the text of the display box to the parameter.
     * @param menu the menu message to display
     */
    public void setText(String menu){
        SpannedString menuSpanned = new SpannedString(Html.fromHtml(menu));
        display.setText(menuSpanned);
    }


    /**
     * Sets the text of the display box to the given SpannedString parameter
     * @param menu the SpannedString menu message to display
     */
    public void setText(SpannedString menu){
        display.setText(menu);
    }

}
