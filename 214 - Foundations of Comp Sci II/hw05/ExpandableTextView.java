package com.example.tictactoeai.custom;

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
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Project Imports
import com.example.tictactoeai.R;


/* ---------------------------------------- CLASS DEF. ---------------------------------------- */

public class ExpandableTextView extends TextView {

    /* ----------------------------- VARS ----------------------------- */

    private static final int MAX_LINES = 1;
    private boolean collapsed = true;
    private static final int COLLAPSED_HEIGHT = 125;
    private static final int EXPANDED_HEIGHT = 300;
    private ResizeAnimation resizer;
    private View.OnClickListener toggleListener;


    /* ----------------------------- CONSTRUCTORS ----------------------------- */

    /**
     * Default constructor for ExpandableTextView
     * @param context   the context we are in
     */
    public ExpandableTextView(Context context){
        super(context);
        setMaxLines(MAX_LINES);
        setText(context.getString(R.string.empty));
        resizer = new ResizeAnimation(this);
        resizer.setWidths(getMeasuredWidth(),getMeasuredWidth());
        resizer.setDuration(1000);
        toggleListener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                toggle();
            }
        };
    }

    /**
     * Deetailed constructor for ExpandableTextView
     * @param context   the context we are in
     * @param attrs     custom attributes
     */
    public ExpandableTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        setMaxLines(MAX_LINES);
        setText(context.getString(R.string.empty));
        resizer = new ResizeAnimation(this);
        resizer.setWidths(getMeasuredWidth(),getMeasuredWidth());
        resizer.setDuration(1000);
        toggleListener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                toggle();
            }
        };
    }


    /* ----------------------------- METHODS ----------------------------- */

    /**
     * Makes this ExpandableTextView expandable by setting an onClickListener.
     */
    public void makeExpandable(){
        setOnClickListener(toggleListener);
    }

    /**
     * Toggles expansion/collapse of the ExpandableTextView.
     */
    public void toggle(){
        if (collapsed){
            resizer.setHeights(COLLAPSED_HEIGHT,EXPANDED_HEIGHT);
            this.startAnimation(resizer);
            this.setMaxLines(Integer.MAX_VALUE);
            collapsed = false;
        }
        else {
            resizer.setHeights(EXPANDED_HEIGHT,COLLAPSED_HEIGHT);
            this.startAnimation(resizer);
            this.setMaxLines(MAX_LINES);
            collapsed = true;
        }
    }

}