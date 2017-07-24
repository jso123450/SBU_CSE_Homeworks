package com.example.kbcalculator.adapters;


/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// Java Imports
import java.util.ArrayList;
import java.util.List;

// Project Imports
import com.example.kbcalculator.R;
import com.example.kbcalculator.source.*;


/* ----------------------------- CLASS DEF ----------------------------- */

public class ListAdapters {

    public static class ActorAdapter extends BaseAdapter {
        /* ----------------------- NESTED CLASS DEF ----------------------- */

        /**
         * Implementation of the ViewHolder design pattern to facilitate smooth scrolling in ListViews.
         */
        private static class ViewHolder {
            Actor actor;
            TextView textView;
            int position;
        }


    /* ----------------------- INSTANCE VAR ----------------------- */

        // DATA VARS
        private Context mContext;
        private List<Actor> actors;


    /* ----------------------- CONSTRUCTORS ----------------------- */

        /**
         * Default ListAdapter constructor.
         * @param context            the context we are in
         */
        public ActorAdapter(Context context) {
            mContext = context;
            actors = new ArrayList<Actor>();
        }

        /**
         * More detailed ListAdapter constructor.
         * @param context   context we are in
         * @param actors    the items this Adapter will represent
         */
        public ActorAdapter(Context context, List<Actor> actors) {
            mContext = context;
            this.actors = actors;
        }


    /* ----------------------- METHODS ----------------------- */

        public int getCount() {
            return actors.size();
        }

        public Object getItem(int position) {
            return actors.get(position);
        }

        public long getItemId(int position) {
            return (position);
        }

        public List<Actor> getData(){
            return this.actors;
        }

        public void setData(List<Actor> actors){
            this.actors = actors;
        }

        /**
         * Main method of this ListAdapter that handles displaying.
         * @param position      index of the represented data value
         * @param convertView   the view
         * @param parent        the parent ViewGroup
         * @return              a (new) TextView with corresponding text
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            ViewHolder holder;

            if (convertView == null) {
                convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.listview_element, parent, false);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.listview_element_text);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            Actor actor = actors.get(position);
            if (actor != null){
                holder.position = position;
                holder.actor = actor;
                holder.textView.setText(Html.fromHtml(actor.toSpinnerString()));
            }

            convertView.setTag(holder);
            return convertView;

        }
    }

    public static class MovieAdapter extends BaseAdapter {

        /* ----------------------- NESTED CLASS DEF ----------------------- */

        /**
         * Implementation of the ViewHolder design pattern to facilitate smooth scrolling in ListViews.
         */
        private static class ViewHolder {
            Movie movie;
            TextView textView;
            int position;
        }


    /* ----------------------- INSTANCE VAR ----------------------- */

        // DATA VARS
        private Context mContext;
        private List<Movie> movies;


    /* ----------------------- CONSTRUCTORS ----------------------- */

        /**
         * Default ListAdapter constructor.
         * @param context            the context we are in
         */
        public MovieAdapter(Context context) {
            mContext = context;
            movies = new ArrayList<Movie>();
        }

        /**
         * More detailed ListAdapter constructor.
         * @param context   context we are in
         * @param movies    the items this Adapter will represent
         */
        public MovieAdapter(Context context, List<Movie> movies) {
            mContext = context;
            this.movies = movies;
        }


    /* ----------------------- METHODS ----------------------- */

        public int getCount() {
            return movies.size();
        }

        public Object getItem(int position) {
            return movies.get(position);
        }

        public long getItemId(int position) {
            return (position);
        }

        public List<Movie> getData(){
            return this.movies;
        }

        public void setData(List<Movie> movies){
            this.movies = movies;
        }

        /**
         * Main method of this ListAdapter that handles displaying.
         * @param position      index of the represented data value
         * @param convertView   the view
         * @param parent        the parent ViewGroup
         * @return              a (new) TextView with corresponding text
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            ViewHolder holder;

            if (convertView == null) {
                convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.listview_element, parent, false);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.listview_element_text);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            Movie movie = movies.get(position);
            if (movie != null){
                holder.position = position;
                holder.movie = movie;
                holder.textView.setText(Html.fromHtml(movie.toString()));
            }

            convertView.setTag(holder);
            return convertView;

        }
    }

}
