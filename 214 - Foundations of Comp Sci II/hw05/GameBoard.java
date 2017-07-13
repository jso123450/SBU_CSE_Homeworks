package com.example.tictactoeai.source;

/**
 * Johnny So
 * 111158276
 * Homework #5
 * CSE 214 Recitation 12
 * Recitation TA: Charles Chen
 * Grading TA: Timothy Zhang
 * @author Johnny
 */


/* ----------------------------- IMPORTS ----------------------------- */





/* ----------------------------- CLASS DEF ----------------------------- */

public class GameBoard {

    /* ----------------------- VARS ----------------------- */

    private Box[] board;
    private static final int BOARD_SIZE = 9;

    /* ----------------------- CONSTRUCTORS ----------------------- */

    /**
     * Default GameBoard constructor. Initializes the board to the appropriate size.
     * @return a GameBoard
     */
    public GameBoard(){
        board = new Box[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            board[i] = Box.EMPTY;
    }

    /**
     * Modified GameBoard constructor that takes the current state of the board.
     * @param  boardState    current state of the board
     * @return               [description]
     */
    public GameBoard(Box[] boardState){
        this.board = boardState;
    }


    /* ----------------------- METHODS ----------------------- */

    /**
     * Retrieves the current board state of the GameBoard.
     * @return the current board state
     */
    public Box[] getBoard(){
        return this.board;
    }

    /**
     * Modifies the current board state.
     * @param boardState the modified board state
     */
    public void setBoard(Box[] boardState){
        this.board = boardState;
    }

    /**
     * Retrieves the board size.
     * @return the board size of this GameBoard
     */
    public static int getBoardSize(){
        return BOARD_SIZE;
    }

    /**
     * Checks if the given position is valid
     * @param  position      the position to be checked
     * @return               true if valid, false otherwise
     */
    public boolean checkSpot(int position){
        return (!(board[position] != Box.EMPTY || position < 0 || position > 8));
    }

    /**
     * Sets the board position as specified.
     * @param position the position to be set
     * @param turn     the turn to set
     */
    public void setMove(int position, Box turn){
        if (checkSpot(position))
            board[position] = turn;
    }

    /**
     * Returns a deep clone of GameBoard's Box[] board.
     */
    public GameBoard clone(){
        Box[] copyBoard = new Box[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            copyBoard[i] = board[i];
        return new GameBoard(copyBoard);
    }
}
