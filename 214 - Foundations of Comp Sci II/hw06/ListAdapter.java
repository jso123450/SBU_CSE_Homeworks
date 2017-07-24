package com.example.bitterplatform.custom;

/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// Java Imports
import java.util.List;

// Project Imports
import com.example.bitterplatform.R;
import com.example.bitterplatform.source.*;


/* ----------------------------- CLASS DEF ----------------------------- */


public class ListAdapter extends BaseAdapter {

    /* ----------------------- NESTED CLASS DEF ----------------------- */

    /**
     * Implementation of the ViewHolder design pattern to facilitate smooth scrolling in ListViews.
     */
    static class ViewHolder {
        User user;
        TextView textView;
        int position;
    }


    /* ----------------------- INSTANCE VAR ----------------------- */

    // DATA VARS
    private Context mContext;
    private User[] users;


    /* ----------------------- CONSTRUCTORS ----------------------- */

    /**
     * Default ListAdapter constructor.
     * @param context            the context we are in
     */
    public ListAdapter(Context context) {
        mContext = context;
        users = new User[0];
    }

    /**
     * More detailed ListAdapter constructor.
     * @param context   context we are in
     * @param items     the items this Adapter will represent
     */
    public ListAdapter(Context context, User[] items) {
        mContext = context;
        this.users = items;
    }


    /* ----------------------- METHODS ----------------------- */

    public int getCount() {
        return users.length;
    }

    public Object getItem(int position) {
        return users[position];
    }

    public long getItemId(int position) {
        return (position);
    }

    public User[] getData(){
        return this.users;
    }

    public void setData(User[] users){
        this.users = users;
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

        User user = users[position];
        if (user != null){
            holder.position = position;
            holder.user = user;
            holder.textView.setText(Html.fromHtml(user.toString()));
        }

        convertView.setTag(holder);
        return convertView;

    }

}
