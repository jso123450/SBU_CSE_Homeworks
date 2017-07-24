package com.example.kbcalculator.adapters;



/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

// Java Imports
import java.util.ArrayList;
import java.util.List;

// Project Imports
import com.example.kbcalculator.source.*;

/* ----------------------------- CLASS DEF ----------------------------- */

public class SpinnerAdapters {

    public static class ActorAdapter implements android.widget.SpinnerAdapter {

        /* ----------------------- INSTANCE VAR ----------------------- */

        private Context context;
        private List<Actor> actors;

        /* ----------------------- CONSTRUCTORS ----------------------- */

        public ActorAdapter(Context context){
            this.context = context;
            this.actors = new ArrayList<Actor>();
        }

        public ActorAdapter(Context context, List<Actor> actors){
            this.context = context;
            this.actors = actors;
        }


        /* ----------------------- METHODS ----------------------- */

        @Override
        public int getCount() {
            return actors.size();
        }

        @Override
        public Object getItem(int position) {
            return actors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (position);
        }

        public List<Actor> getData(){
            return this.actors;
        }

        public void setData(List<Actor> actors){
            this.actors = actors;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
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
            return actors.isEmpty();
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
            textView.setText(actors.get(position).toSpinnerString());
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            return textView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, null);
            }
            ((TextView) convertView).setText(actors.get(position).toSpinnerString());
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


}
