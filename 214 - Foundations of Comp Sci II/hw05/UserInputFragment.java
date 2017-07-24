package com.example.tictactoeai.fragments;



/* ---------------------------------------- IMPORTS ---------------------------------------- */

// Android Imports
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

// Java Imports
import java.util.ArrayList;
import java.util.List;

// Project Imports
import com.example.tictactoeai.R;
import com.example.tictactoeai.custom.*;



/* ---------------------------------------- CLASS DEF. ---------------------------------------- */


public class UserInputFragment extends Fragment {

    /* ----------------------- VARS ----------------------- */

    private Spinner menuSpinner;
    private EditText input1;

    /* ----------------------- INITIALIZATION ----------------------- */
    /**
     * Required lifecycle method.
     * @param inflater              the layout inflater
     * @param container             the ViewGroup container
     * @param savedInstanceState    the instance state of the app
     * @return                      the inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View inflatedView = inflater.inflate(R.layout.fragment_user_input, container, false);
        menuSpinner = (Spinner) inflatedView.findViewById(R.id.menu_selection);
        input1 = (EditText) inflatedView.findViewById(R.id.input1);
        ArrayAdapter<CharSequence> menuAdapter = ArrayAdapter.createFromResource(inflatedView.getContext(), R.array.which_turn, android.R.layout.simple_spinner_item);
        menuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menuSpinner.setAdapter(menuAdapter);
        menuSpinner.setVisibility(View.GONE);
        return inflatedView;
    }



}
