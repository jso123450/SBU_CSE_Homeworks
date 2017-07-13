package com.example.tictactoeai.source;

import java.util.Arrays;

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


public class GameTree {

    /* ----------------------- INSTANCE VAR ----------------------- */

    private GameBoardNode root;
    private GameBoardNode cursor;
    private double[][] cursorConfigProbs;

    private static Box PLAYERS_TURN = Box.X;
    private static Box AI_TURN = Box.O;

    private static final int WIN_INDEX = 0;
    private static final int DRAW_INDEX = 1;
    private static final int LOSE_INDEX = 2;

    /* ----------------------- CONSTRUCTORS ----------------------- */

    /**
     * Default GameTree constructor.
     */
    public GameTree(){
        this.root = new GameBoardNode(new GameBoard(), PLAYERS_TURN);
        this.cursor = this.root;
        cursorConfigProbs = new double[9][3];
    }

    /**
     * GameTree constructor that will enable the user to specify his type.
     * @param playersTurn selected players' turn (Box.X or Box.O)
     */
    public GameTree(Box playersTurn){
        PLAYERS_TURN = playersTurn;
        AI_TURN = ((PLAYERS_TURN == Box.X)? Box.O : Box.X);
        this.root = new GameBoardNode(new GameBoard(), PLAYERS_TURN);
        this.cursor = this.root;
        cursorConfigProbs = new double[9][3];
    }


    /* ----------------------- METHODS ----------------------- */

    /**
     * Retrieves the root of this GameTree.
     * @return the root of this GameTree
     */
    public GameBoardNode getRoot(){
        return this.root;
    }

    /**
     * Retrieves the cursor of this GameTree.
     * @return the cursor of this GameTree
     */
    public GameBoardNode getCursor(){
        return this.cursor;
    }

    /**
     * Sets the cursor of this GameTree.
     * @param cursor the new cursor
     */
    public void setCursor(GameBoardNode cursor) { this.cursor = cursor; }

    /**
     * Makes the move as specified by the cursor's currentTurn and the position.
     * @param position  the position of the desired move
     */
    public void makeMove(int position) throws IllegalMoveException {
        GameBoard currentBoard = cursor.getBoard();

        // if not a valid spot, throw an exception
        if (!currentBoard.checkSpot(position))
            throw new IllegalMoveException();

        cursor = cursor.getConfig()[position];
        cursor.setProbabilities();
        for (int i = 0; i < cursorConfigProbs.length; i++){
            GameBoardNode child = cursor.getConfig()[i];
            if (child instanceof GameBoardNode){
                child.setProbabilities();
                cursorConfigProbs[i] = child.getProbs();
            }
            else
                cursorConfigProbs[i] = new double[]{1000.0,-5000.0,-1000.0};  // for null nodes
        }

    }

    /**
     * Will be used to check for a double trap by returning an array of the players' moves.
     * Usage: if return length is two, check if they are in the opposite corners and decide
     * the move accordingly in AIPlayGame().
     * @return an array of position ints of the players' moves
     */
    private int[] checkDoubleTrap(){
        int numPlayerMoves = cursor.getNumPlayerMoves();
        int[] playerMovePos = new int[numPlayerMoves];
        int playerMovePosCounter = 0;
        for (int i = 0; i < cursor.getBoard().getBoard().length; i++){
            if (cursor.getBoard().getBoard()[i] == PLAYERS_TURN){
                playerMovePos[playerMovePosCounter] = i;
                playerMovePosCounter++;
            }
        }
        return playerMovePos;
    }

    /**
     * Builds the GameTree starting from the root node and turn.
     * @param  root          the root of the subtree
     * @param  turn          the current turn at this root
     * @return               the root of the subtree built tree
     */
    public static GameBoardNode buildTree(GameBoardNode root, Box turn) throws IllegalMoveException {
        // builds the tree starting from root and the current turn
        if ((root == null) || (root.getIsEnd()))
            return root;
        else {
            root.buildConfig();
            Box nextTurn = ((turn == PLAYERS_TURN) ? AI_TURN : PLAYERS_TURN);
            for (GameBoardNode child : root.getConfig()){
                buildTree(child,nextTurn);
            }
            return root;
        }
    }

    /**
     * Finds the number of times an element occurs in an Array.
     * @param  number        the element to check
     * @param  array         the Array to check occurrences in
     * @return               the number of occurrences of element in array
     */
    private int findOccurrences(double number, double[] array){
        int occurrences = 0;
        for (int i = 0; i < array.length; i++){
            if (array[i] == number)
                occurrences++;
        }
        return occurrences;
    }

    private int[] findOccurrencesPositions(double number, double[] array){
        int[] occurrencesPos = new int[9];
        int occurrencesPosLen = 0;
        for (int i = 0; i < array.length; i++){
            if (array[i] == number){
                occurrencesPos[occurrencesPosLen] = i;
                occurrencesPosLen++;
            }
        }
        if (occurrencesPosLen == 0)
            return null;
        int[] returnPos = new int[occurrencesPosLen];
        for (int i = 0; i < occurrencesPosLen; i++){
            returnPos[i] = occurrencesPos[i];
        }
        return returnPos;
    }

    /**
     * Finds the indices of repeated elements in an array.
     * @param  number       the repeated element
     * @param  array        the Array to check
     * @return              an int[] of the indices of repeated elements
     */
    private int[] indicesOfRep(double number, double[] array){
        int occurrences = findOccurrences(number,array);
        int[] indices = new int[occurrences];
        int indicesIndex = 0;
        for (int i = 0; i < array.length; i++){
            if (cursor.getConfig()[i] instanceof GameBoardNode){
                if (array[i] == number){
                    indices[indicesIndex] = i;
                    indicesIndex++;
                }
            }
        }
        return indices;
    }


    /**
     * The method that will calculate probabilities and decide on a move.
     * @return                      true if the AI made a move
     * @throws IllegalMoveException if illegal position (realistically should never be thrown here)
     */
    public boolean AIPlayGame() throws IllegalMoveException{
        if ((cursor instanceof GameBoardNode) && (!cursor.getIsEnd()) && (cursor.getCurrentTurn() == AI_TURN)){
            // grab the player's moves
            int[] playerMovePos = checkDoubleTrap();
            if (playerMovePos.length == 2){
                // check for double trap
                if (playerMovePos[0] == 0 && playerMovePos[1] == 8 && cursor.getBoard().getBoard()[1] == Box.EMPTY) {
                    makeMove(1);
                    return true;
                }
                if (playerMovePos[0] == 2 && playerMovePos[1] == 6 && cursor.getBoard().getBoard()[7] == Box.EMPTY) {
                    makeMove(7);
                    return true;
                }
                // check for middle start, trying to go horizontal or vertical
                if (playerMovePos[0] == 4 || playerMovePos[1] == 4){
                    if (playerMovePos[0] == 4){
                        if (cursor.getBoard().getBoard()[playerMovePos[0] - (playerMovePos[1] - playerMovePos[0])] == Box.EMPTY) {
                            makeMove(playerMovePos[0] - (playerMovePos[1] - playerMovePos[0]));
                            return true;
                        }
                    }
                    else if (cursor.getBoard().getBoard()[playerMovePos[1] + (playerMovePos[1] - playerMovePos[0])] == Box.EMPTY) {
                        makeMove(playerMovePos[1] + (playerMovePos[1] - playerMovePos[0]));
                        return true;
                    }
                }
            }

            // arrays that hold only win and lose probs
            double[] winProbs = new double[9];
            double[] loseProbs = new double[9];

            // an array of the indices of the non-null elements in the cursor's config
            int[] nonNullChildren = new int[9];
            int nonNullChildrenSize = 0;

            // fill up the arrays accordingly
            for (int i = 0; i < cursorConfigProbs.length; i++){
                if (cursor.getConfig()[i] instanceof GameBoardNode){
                    nonNullChildren[nonNullChildrenSize] = i;
                    nonNullChildrenSize++;
                }
                winProbs[i] = cursorConfigProbs[i][WIN_INDEX];
                loseProbs[i] = cursorConfigProbs[i][LOSE_INDEX];
            }



            // loop through to find the smallest win prob and highest lose prob and respective indices
            int smallestWinChanceIndex = -1;
            double smallestWinChance = 1.0;
            int highestLoseChanceIndex = -1;
            double highestLoseChance = 0.0;
            for (int i = 0; i < nonNullChildrenSize; i++){
                if (winProbs[nonNullChildren[i]] < smallestWinChance){
                    smallestWinChance = winProbs[nonNullChildren[i]];
                    smallestWinChanceIndex = i;
                }
                if (loseProbs[nonNullChildren[i]] > highestLoseChance){
                    highestLoseChance = loseProbs[nonNullChildren[i]];
                    highestLoseChanceIndex = i;
                }
            }


            // find the number of times each one occurs
            int smallestWinChanceRep = findOccurrences(smallestWinChance, winProbs);
            int highestLoseChanceRep = findOccurrences(highestLoseChance, loseProbs);

            // if there are multiple nodes with the same probabilities, move to a random one
            boolean multiple = false;
            int[] sameWinProbs = findOccurrencesPositions(smallestWinChance,winProbs);
            int[] sameLoseProbs = findOccurrencesPositions(highestLoseChance,loseProbs);
            int[] sameProbs = new int[9];
            int sameProbsLen = 0;
            for (int i = 0; i < sameWinProbs.length; i++){
                for (int j = 0; j < sameLoseProbs.length; j++){
                    if (sameWinProbs[i] == sameLoseProbs[j]) {
                        sameProbs[sameProbsLen] = sameWinProbs[i];
                        sameProbsLen++;
                    }
                }
            }
            if (sameProbsLen > 0){
                makeMove(sameProbs[(int)(Math.random()*sameProbsLen)]);
                return true;
            }


            // if smallest win chance occurs only once, go to that child
            if (smallestWinChanceRep == 1 || (smallestWinChance == 0.0 && loseProbs[nonNullChildren[smallestWinChanceIndex]] == 1.0))
                makeMove(nonNullChildren[smallestWinChanceIndex]);
            else {
                // else go to the child with the smallest win chance and highest lose chance
                int smallestWHighestLChanceIndex = nonNullChildren[smallestWinChanceIndex];
                double smallestWHighestLChance = loseProbs[nonNullChildren[smallestWinChanceIndex]];
                int[] indicesOfWinChance = indicesOfRep(smallestWinChance,winProbs);
                for (int i = 0; i < indicesOfWinChance.length; i++){
                    if (loseProbs[indicesOfWinChance[i]] >= smallestWHighestLChance){
                        smallestWHighestLChanceIndex = indicesOfWinChance[i];
                        smallestWHighestLChance = loseProbs[indicesOfWinChance[i]];
                    }
                }
                makeMove(smallestWHighestLChanceIndex);
            }

            return true;
        }
        else
            return false;
    }

    /**
     * Checks if the node passed is a leaf and who has won.
     * @param  node          the node to be checked
     * @return               null if not a leaf, empty if draw, winner's symbol otherwise
     */
    public static Box checkWin(GameBoardNode node){
        if (node instanceof GameBoardNode)
            return node.checkWinner();
        return null;
    }

    /**
     * Retrieves the winning probability at the current state of the game
     * @return the winning probability of the cursor
     */
    public double cursorProbability(){
        return cursor.getWinProb();
    }

    /**
     * Retrieves the losing probability at the current state of the game.
     * @return the losing probability of the cursor
     */
    public double cursorLoseProb(){
        return cursor.getLoseProb();
    }

    /**
     * Retrieves the draw probability at the current state of the game.
     * @return the draw probability of the cursor
     */
    public double cursorDrawProb(){
        return cursor.getDrawProb();
    }

    /**
     * Undoes the last move, not necessarily the player's last move.
     * @return true if successful, false if illegal
     */
    public boolean undo(){
        if (cursor.getParent() instanceof GameBoardNode) {
            cursor = cursor.getParent();
            return true;
        }
        return false;
    }

    /**
     * Stringifies the cursor.
     * @return the cursor
     */
    public String toString(){
        String output = "cursor: \n";
        output+= this.cursor + "\n";
        return output;
    }

}
