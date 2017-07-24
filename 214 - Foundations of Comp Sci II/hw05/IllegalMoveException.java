package com.example.tictactoeai.source;


 /* ----------------------------- CLASS DEF ----------------------------- */

public class IllegalMoveException extends Exception {

    /* ----------------------- CONSTRUCTORS ----------------------- */

    public IllegalMoveException(){
        super("Sorry, this move is illegal.");
    }

    public IllegalMoveException(String errorMessage){
        super(errorMessage);
    }


}
