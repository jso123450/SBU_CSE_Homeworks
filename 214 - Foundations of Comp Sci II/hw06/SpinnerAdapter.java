package com.example.bitterplatform.custom;
/**
 * Johnny So
 * 111158276
 * Homework #6
 * CSE 214 Recitation 12
 * Recitation TA: Charles Chen
 * Grading TA: Tim Zhang
 * @author Johnny
 */


/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.bitterplatform.source.User;

/* ----------------------------- CLASS DEF ----------------------------- */

public class SpinnerAdapter implements android.widget.SpinnerAdapter {

    /* ----------------------- INSTANCE VAR ----------------------- */

    private Context context;
    private User[] users;

    /* ----------------------- CONSTRUCTORS ----------------------- */

    public SpinnerAdapter(Context context){
        this.context = context;
        this.users = new User[0];
    }

    public SpinnerAdapter(Context context, User[] data){
        this.context = context;
        this.users = data;
    }


    /* ----------------------- METHODS ----------------------- */

    @Override
    public int getCount() {
        return users.length;
    }

    @Override
    public Object getItem(int position) {
        return users[position];
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    public User[] getData(){
        return this.users;
    }

    public void setData(User[] users){
        this.users = users;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return users.length == 0;
    }

    /**
     * Main method that handles displaying the data.
     * @param position      the index of the data element
     * @param convertView   the previous view if any
     * @param parent        the ViewGroup parent
     * @return              a (new) TextView as specified by the underlying data element
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TextView textView;
        if (convertView == null)
            textView = (TextView) View.inflate(context, android.R.layout.simple_spinner_item, null);
        else
            textView = (TextView) convertView;
        textView.setText(users[position].toSpinnerString());
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        }
        ((TextView) convertView).setText(users[position].toSpinnerString());
        ((TextView) convertView).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        // do nothing
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        // do nothing
    }

}
