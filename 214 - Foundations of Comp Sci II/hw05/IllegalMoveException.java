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
