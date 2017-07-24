package com.example.tictactoeai.custom;


/* ----------------------------- IMPORTS ----------------------------- */

// Android Imports
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

// Project Imports
import com.example.tictactoeai.R;
import com.example.tictactoeai.source.*;


/* ----------------------------- CLASS DEF ----------------------------- */

public class GridAdapter extends BaseAdapter {

    /* ----------------------------- VARS ----------------------------- */

    private Context mContext;
    private GameBoardNode mNode;
    private final int NUM_COLUMNS = 3;
    private Box PLAYERS_TURN;
    private final static int BOARD_SIZE = 9;


    /* ----------------------------- CONSTRUCTORS ----------------------------- */

    /**
     * Default GridAdapter constructor
     * @param context   the context we are in
     */
    public GridAdapter(Context context) {
        mContext = context;
        mNode = null;
        PLAYERS_TURN = Box.X;
    }

    /**
     * More detailed GridAdapter constructor.
     * @param context   the context we are in
     * @param node      the GameBoardNode that the GridView will represent
     */
    public GridAdapter(Context context, GameBoardNode node){
        mContext = context;
        mNode = node;
        PLAYERS_TURN = Box.X;
    }

    /**
     * More detailed GridAdapter constructor.
     * @param context       the context we are in
     * @param node          the GameBoardNode that the GridView will represent
     * @param playersTurn   the player's turn
     */
    public GridAdapter(Context context, GameBoardNode node, Box playersTurn){
        mContext = context;
        mNode = node;
        PLAYERS_TURN = playersTurn;
    }


    /* ----------------------------- METHODS ----------------------------- */

    // Required BaseAdapter methods

    public int getCount() { return BOARD_SIZE; }

    public Object getItem(int position) {
        return mNode.getConfig()[position];
    }

    public long getItemId(int position) {
        return (position/NUM_COLUMNS);
    }

    public void setData(GameBoardNode node){
        mNode = node;
    }

    public void setPlayersTurn(Box playersTurn) { this.PLAYERS_TURN = playersTurn; }

    public Box getPlayersTurn(){ return this.PLAYERS_TURN; }

    /**
     * Returns the view requested by the adapter after every time it is notified that its
     * data set has been updated.
     * @param position      the position of the item in the data
     * @param convertView   the view (can be null) that is there
     * @param parent        the parent viewgroup
     * @return              the View as specified
     */
    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView;
        // if null, make a new view
        if (convertView == null)
            imageView = new ImageView(mContext);
        else
            imageView = (ImageView) convertView;    // else cast it as an ImageView

        // set its layout parameters
        imageView.setLayoutParams(new GridView.LayoutParams(250,250));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(0, 0, 0, 0);

        // the ImageView we will be using depends on what it represents
        Box move = mNode.getBoard().getBoard()[position];
        if (move != Box.EMPTY) {
            imageView.setClickable(false);
            int whichType;
            // the player is always Box.X in the underlying data and tree
            if (move == Box.X)
                whichType = ((PLAYERS_TURN == Box.X)? 0 : 1);
            else
                whichType = ((PLAYERS_TURN == Box.X)? 1 : 0);
            imageView.setImageResource(mThumbIds[whichType]);
        }
        else    //if empty, set it as an empty square
            imageView.setImageResource(mThumbIds[2]);
        return imageView;
    }


    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.x_mark,
            R.drawable.o_mark,
            R.drawable.square
    };
}
