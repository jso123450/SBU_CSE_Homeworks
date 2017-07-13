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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;


/* ---------------------------------------- CLASS DEF. ---------------------------------------- */

public class ResizeAnimation extends Animation {


    /* ----------------------------- VARS ----------------------------- */

    private int startHeight, deltaHeight;
    private int startWidth, deltaWidth;

    private View view;

    /* ----------------------------- CONSTRUCTORS ----------------------------- */

    /**
     * Default constructor for a ResizeAnimation.
     * @param view  the view to resize
     */
    public ResizeAnimation(View view) {
        this.view = view;
    }

    /**
     * More detailed constructor for a ResizeAnimation.
     * @param view          the view to resize
     * @param startHeight   the starting height
     * @param endHeight     the ending height
     */
    public ResizeAnimation(View view, int startHeight, int endHeight){
        this.view = view;
        setHeights(startHeight,endHeight);
    }

    /**
     * Most detailed constructor for a ResizeAnimation.
     * @param view          the view to resize
     * @param startHeight   the starting height
     * @param endHeight     the ending height
     * @param startWidth    the starting width
     * @param endWidth      the ending width
     */
    public ResizeAnimation(View view, int startHeight, int endHeight, int startWidth, int endWidth){
        this.view = view;
        setHeights(startHeight,endHeight);
        setWidths(startWidth,endWidth);
    }


    /* ----------------------------- METHODS ----------------------------- */

    /**
     * Sets the start and ending heights.
     * @param start the start height
     * @param end   the ending height
     */
    public void setHeights(int start, int end) {
        this.startHeight = start;
        this.deltaHeight = end - this.startHeight;
    }

    /**
     * Sets the start and ending widths.
     * @param start the start width
     * @param end   the ending width
     */
    public void setWidths(int start, int end) {
        this.startWidth = start;
        this.deltaWidth = end - this.startWidth;
    }

    /**
     * Applying the transformation for each little time step.
     * @param interpolatedTime  the small time step that will be used
     * @param t                 the transformation
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        // adjust heights accordingly
        if (startHeight != 0) {
            if (deltaHeight > 0) {
                view.getLayoutParams().height = (int) (startHeight + deltaHeight * interpolatedTime);
            } else {
                view.getLayoutParams().height = (int) (startHeight - Math.abs(deltaHeight) * interpolatedTime);
            }
        }
        // adjust widths accordingly
        if (startWidth != 0) {
            if (deltaWidth > 0) {
                view.getLayoutParams().width = (int) (startWidth + deltaWidth * interpolatedTime);
            } else {
                view.getLayoutParams().width = (int) (startWidth - Math.abs(deltaWidth) * interpolatedTime);
            }
        }

        view.requestLayout();
    }
}