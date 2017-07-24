package com.example.tictactoeai.source;



/* ----------------------------- IMPORTS ----------------------------- */





/* ----------------------------- CLASS DEF ----------------------------- */

public class GameBoardNode {

    /* ----------------------- INSTANCE VAR ----------------------- */

    private GameBoard board;
    private boolean isEnd;
    private Box currentTurn;            // X or O?
    private Box winner;                 // Box.EMPTY if draw
    private GameBoardNode[] config;
    private GameBoardNode parent;
    private double[] probs;
    private double winProb;
    private double loseProb;
    private double drawProb;

    // number of winning/losing/draw leaves of the subtree from this node
    private int winLeaves = 0;
    private int drawLeaves = 0;
    private int loseLeaves = 0;

    // indices that describe each index of the array returned by getNumLeaves(node)
    private static final int WIN_INDEX = 0;
    private static final int DRAW_INDEX = 1;
    private static final int LOSE_INDEX = 2;

    /* ----------------------- CONSTRUCTORS ----------------------- */

    /**
     * Default GameBoardNode constructor.
     * @param  board         the state of the board
     * @param  currentTurn   the current turn as of this board state
     * @return               the GameBoardNode as specified
     */
    public GameBoardNode(GameBoard board, Box currentTurn){
        if (currentTurn == Box.EMPTY || board == null)
            throw new IllegalArgumentException();
        this.board = board;
        this.currentTurn = currentTurn;
        this.config = new GameBoardNode[9];
        this.winner = null;
        this.probs = new double[3];
        this.winProb = 0.0;
        this.loseProb = 0.0;
        this.drawProb = 0.0;
        this.parent = null;
        checkWinner();
    }

    /**
     * Default GameBoardNode constructor.
     * @param  board         the state of the board
     * @param  currentTurn   the current turn as of this board state
     * @param  parent        the parent of this node
     * @return               the GameBoardNode as specified
     */
    public GameBoardNode(GameBoard board, Box currentTurn, GameBoardNode parent){
        if (currentTurn == Box.EMPTY || board == null)
            throw new IllegalArgumentException();
        this.board = board;
        this.currentTurn = currentTurn;
        this.config = new GameBoardNode[9];
        this.winner = null;
        this.probs = new double[3];
        this.winProb = 0.0;
        this.loseProb = 0.0;
        this.drawProb = 0.0;
        this.parent = parent;
        checkWinner();
    }


    /* ----------------------- METHODS ----------------------- */

    /**
     * Retrieves the current GameBoard.
     * @return the current GameBoard
     */
    public GameBoard getBoard(){
        return this.board;
    }

    /**
     * Retrieves isEnd.
     * @return true if the game has ended
     */
    public boolean getIsEnd(){
        return this.isEnd;
    }

    /**
     * Retrieves the current turn.
     * @return the current turn
     */
    public Box getCurrentTurn(){
        return this.currentTurn;
    }

    /**
     * Retrieves the winner of the current GameBoard.
     * @return the winner of the current GameBoard
     */
    public Box getWinner(){
        return this.winner;
    }

    /**
     * Retrieves the current config of the current GameBoard.
     * @return the current config of the board
     */
    public GameBoardNode[] getConfig(){
        return this.config;
    }

    /**
     * Retrieves the winning probability at this GameBoardNode.
     * @return the current winning probability
     */
    public double getWinProb(){
        return this.winProb;
    }

    /**
     * Retrieves the losing probability at this GameBoardNode.
     * @return the current losing probability
     */
    public double getLoseProb(){
        return this.loseProb;
    }

    /**
     * Retrieves the probability of making a draw at this GameBoardNode.
     * @return the current drawing probability
     */
    public double getDrawProb(){
        return this.drawProb;
    }

    /**
     * Retrieves the probabilities to win, draw, and lose as specified by the
     * indices WIN_INDEX, DRAW_INDEX, and LOSE_INDEX.
     */
    public double[] getProbs(){
        return this.probs;
    }

    /**
     * Retrieves the parent node of this GameBoardNode.
     * @return the parent node of this GameBoardNode
     */
    public GameBoardNode getParent(){
        return this.parent;
    }

    /**
     * Retrieves the number of children of this GameBoardNode.
     * @return the number of non-null nodes in config
     */
    public int getNumChildren(){
        int numChildren = 0;
        for (int i = 0; i < config.length; i++){
            if (config[i] instanceof GameBoardNode)
                numChildren++;
        }
        return numChildren;
    }

    /**
     * Retrieves the number of moves made at the current state of the board.
     * @return non-Box.EMPTY values in the Box[] board
     */
    public int getNumMoves(){
        int numMoves = 0;
        for (int i = 0; i < board.getBoard().length; i++){
            if (board.getBoard()[i] != Box.EMPTY)
                numMoves++;
        }
        return numMoves;
    }

    /**
     * Retrieves the number of moves made by the player.
     * @return number of moevs made by the player
     */
    public int getNumPlayerMoves(){
        int numPlayerMoves = 0;
        for (int i = 0; i < board.getBoard().length; i++){
            if (board.getBoard()[i] == Box.X)
                numPlayerMoves++;
        }
        return numPlayerMoves;
    }

    /**
     * Wrapper for getNumLeaves
     */
    public int[] getNumLeaves(GameBoardNode node){
        return getNumLeaves(node,0,0,0);
    }

    /**
     * Gets the number of leaves at this node.
     */
    private int[] getNumLeaves(GameBoardNode node, int winL, int drawL, int loseL){
        int[] leaves = new int[]{winL,drawL,loseL};
        if (node == null)
            return leaves;
        if (node.getIsEnd()){
            if (node.getWinner() == Box.X)
                leaves[0]+= 1;
            if (node.getWinner() == Box.EMPTY)
                leaves[1]+= 1;
            if (node.getWinner() == Box.O)
                leaves[2]+= 1;
        }
        else {
            for (int i = 0; i < node.getConfig().length; i++){
                GameBoardNode child = node.getConfig()[i];
                if (child instanceof GameBoardNode){
                    int[] childNumLeaves = child.getNumLeaves(child,winL,drawL,loseL);
                    for (int j = 0; j < childNumLeaves.length; j++)
                        leaves[j]+= childNumLeaves[j];
                }
            }
        }
        return leaves;
    }

    /**
     * Sets the winning, losing, and draw probabilities.
     */
    public void setProbabilities(){
        // get the total # of leaves of each type
        int[] leaves = getNumLeaves(this);
        this.winLeaves = leaves[WIN_INDEX];
        this.drawLeaves = leaves[DRAW_INDEX];
        this.loseLeaves = leaves[LOSE_INDEX];
        int totalLeaves = winLeaves + drawLeaves + loseLeaves;

        // winning prob = winning leaves / total leaves
        this.winProb = ((double)winLeaves)/((double)totalLeaves);
        this.probs[WIN_INDEX] = this.winProb;
        // draw prob = draw leaves / total leaves
        this.drawProb = ((double)drawLeaves)/((double)totalLeaves);
        this.probs[DRAW_INDEX] = this.drawProb;
        // losing prob = losing leaves / total leaves
        this.loseProb = ((double)loseLeaves)/((double)totalLeaves);
        this.probs[LOSE_INDEX] = this.loseProb;
    }

    /**
     * Builds the config of possible moves for currentTurn.
     */
    public void buildConfig() throws IllegalMoveException {
        Box nextTurn = ((currentTurn == Box.X)? Box.O : Box.X);
        Box[] boxBoard = board.getBoard();
        for (int i = 0; i < boxBoard.length; i++){
            if (boxBoard[i] == Box.EMPTY){
                GameBoard configBoard = board.clone();
                configBoard.setMove(i,currentTurn); // should never throw the exception
                GameBoardNode configNode = new GameBoardNode(configBoard,nextTurn,this);
                configNode.checkWinner();
                config[i] = configNode;
            }
        }
    }

    /**
     * Retrieves the winner of the GameBoard in this node.
     * @return the winner of the GameBoard
     */
    public Box checkWinner(){

        // initialize output
        Box winnerReturned = null;

        // get the Box array represented by this node
        Box[] boardArray = board.getBoard();

        // check horizontal
        for (int horizontalPos = 1; horizontalPos < boardArray.length; horizontalPos+= 3){
            Box boxAtHPos = boardArray[horizontalPos];
            if ((boxAtHPos != Box.EMPTY) && (boardArray[horizontalPos-1] == boardArray[horizontalPos+1]) && (boardArray[horizontalPos-1] == boxAtHPos))
                winnerReturned = boxAtHPos;
        }

        // check vertical
        for (int verticalPos = 3; verticalPos < boardArray.length-3; verticalPos++){
            Box boxAtVPos = boardArray[verticalPos];
            if ((boxAtVPos != Box.EMPTY) && (boardArray[verticalPos-3] == boardArray[verticalPos+3]) && (boardArray[verticalPos-3] == boxAtVPos))
                winnerReturned = boxAtVPos;
        }

        // check diagonal
        int centerPos = 4;
        Box boxAtCPos = boardArray[centerPos];
        if (boxAtCPos != Box.EMPTY){
            if ((boardArray[centerPos-2] == boardArray[centerPos+2]) && (boardArray[centerPos-2] == boxAtCPos))
                winnerReturned = boxAtCPos;
            if ((boardArray[centerPos-4] == boardArray[centerPos+4]) && (boardArray[centerPos-4] == boxAtCPos))
                winnerReturned = boxAtCPos;
        }

        // if board is full and no previous cases were true, then its a draw
        if ((getNumMoves() == 9) && (winnerReturned == null)){
            winnerReturned = Box.EMPTY;
        }

        this.winner = winnerReturned;

        // logic to determine which won
        if (winnerReturned != null){
            this.isEnd = true;
        }

        // only case is if leaf
        return winnerReturned;
    }

    /**
     * Retrieves the winning, draw, and losing probabilities in a formatted String.
     * @return the winning, draw, and losing probabilities in a formatted HTML String
     */
    public String getDisplayProbs(){
        String output = "Winning Chances : ";
        output+= winProb + "\n Draw Chances : ";
        output+= drawProb + "\n Losing Chances : ";
        output+= loseProb;
        return output;
    }

    /**
     * Stringifies the current GameBoardNode.
     * @return a String representation of GameBoardNode
     */
    @Override
    public String toString(){
        String boardStr = "";
        for (int i = 0; i < board.getBoardSize(); i++){
            boardStr+= "|";
            if (board.getBoard()[i] == Box.EMPTY)
                boardStr+= " ";
            else
                boardStr+= board.getBoard()[i];
            if (i % 3 == 2)
                boardStr+= "|\n";
        }
        return boardStr;
    }

}
