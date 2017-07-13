package com.example.tictactoeai.fragments;

/**
 * Johnny So
 * 111158276
 * Homework #5
 * CSE 214 Recitation 12
 * Recitation TA: Charles Chen
 * Grading TA: Timothy Zhang
 * @author Johnny
 */

/* ---------------------------------------- IMPORTS ---------------------------------------- */

// Android Imports
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

// Project Imports
import com.example.tictactoeai.R;
import com.example.tictactoeai.custom.ExpandableTextView;


/* ---------------------------------------- CLASS DEF. ---------------------------------------- */


public class StatusDisplayFragment extends Fragment {

    /* ----------------------- VARS ----------------------- */

    private ExpandableTextView currentProbs, hoverProbs;

    /* ----------------------- INITIALIZATION ----------------------- */

    /**
     * Required lifecycle method to create the view.
     * @param inflater              the layout inflater
     * @param container             the ViewGroup container
     * @param savedInstanceState    the instance state of the app
     * @return                      the inflated view
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_status_display, container, false);

        currentProbs = (ExpandableTextView) inflatedView.findViewById(R.id.current_node_probs);
        currentProbs.setText(getString(R.string.current_node_probs));
        currentProbs.makeExpandable();
        hoverProbs = (ExpandableTextView) inflatedView.findViewById(R.id.hover_over_probs);
        hoverProbs.setText(getString(R.string.long_press_probs));
        hoverProbs.makeExpandable();

        return inflatedView;
    }


}