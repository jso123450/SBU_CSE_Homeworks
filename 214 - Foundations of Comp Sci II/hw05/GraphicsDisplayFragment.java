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
import android.widget.GridView;


// Project Imports
import com.example.tictactoeai.R;


/* ---------------------------------------- CLASS DEF. ---------------------------------------- */


public class GraphicsDisplayFragment extends Fragment {

    /* ----------------------- VARS ----------------------- */
    private GridView gridView;

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
        View inflatedView = inflater.inflate(R.layout.fragment_graphics_display, container, false);
        gridView = (GridView) inflatedView.findViewById(R.id.gridview);
        gridView.setVisibility(View.GONE);
        return inflatedView;
    }

}
