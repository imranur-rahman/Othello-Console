import java.util.ArrayList;
import java.util.Scanner;

public class Othello {

        public static final int NONE = 0;
        public static final int BLACK = 1;
        public static final int WHITE = 2;

        private static int[][] directions = new int[][]{{-1,-1}, {-1,0}, {-1,1},  {0,1}, {1,1},  {1,0},  {1,-1},  {0, -1}};

        public int[][] board;
        public int length;
        public static Minimax minimax;

        public int nowPlayer = BLACK;

        public boolean[] isThisPlayerComputer = new boolean[2];

        private long startTime;

        /*
        * input: length is an even number
        * goal: create the initial state of the game
        * */
        public Othello(int length) {
                this.length = length;
                board = new int[length][length];
                for(int row = 0; row < length; ++row){
                        for(int col = 0; col < length; ++col){
                                board[row][col] = NONE;
                        }
                }
                int mid = length / 2;
                board[mid - 1][mid] = BLACK;
                board[mid][mid - 1] = BLACK;
                board[mid - 1][mid - 1] = WHITE;
                board[mid][mid] = WHITE;
        }

        public boolean isValid(int[][] board, int row, int col, int player) {
                /*
                * If this position has a valid move in any direction for this player
                * then this position is valid for this player
                * */
                for(int dirIndex = 0; dirIndex < directions.length; ++dirIndex){
                        if(hasValidMoveInThisDir(board, player, dirIndex, new Pair(row, col)))
                                return true;
                }
                return false;
        }

        public boolean hasValidMoves(int[][] board, int player) {
                ArrayList<Pair<Integer, Integer>> moves = validMoves(board, player);
                return (moves.size() != 0);
        }

        public void removeMove(int[][] board, int row, int col){
                setElementInBoard(board, new Pair(row, col), NONE);
        }

        public void makeMove(int[][] board, int row, int col, int player) {
                for(int dirIndex = 0; dirIndex < directions.length; ++dirIndex){
                        if(hasValidMoveInThisDir(board, player, dirIndex, new Pair(row, col)))
                                flipInThisDirection(board, player, dirIndex, new Pair(row, col));
                }
                setElementInBoard(board, new Pair(row, col), player);
        }

        public void flipInThisDirection(int[][] board, int player, int dirIndex, Pair<Integer, Integer>nowPosition){
                //System.out.println("flipInThisDirection : " + player + " " + dirIndex);
                while(true) {
                        // update the x, y coordinate
                        nowPosition.setLeft(nowPosition.getLeft() + directions[dirIndex][0]);
                        nowPosition.setRight(nowPosition.getRight() + directions[dirIndex][1]);

                        /*
                        * If we are out of board now,
                        * return
                        * */
                        if(isInBoard(nowPosition) == false) {
                                return;
                        }

                        /*
                        * If this position is empty
                        * then return, our job is done
                        * */
                        if(getElementFromBoard(board, nowPosition) == NONE){
                                return;
                        }

                        /*
                        * If we have found one of this player's disk
                        * then return
                        * */
                        if(getElementFromBoard(board, nowPosition) == player){
                                return;
                        }

                        /*
                        * First get the element in this cell
                        * If the element is not possessed by the current player
                        * make it possessed by the current player
                        * */
                        if(getElementFromBoard(board, nowPosition) != player)
                                setElementInBoard(board, nowPosition, player);

                }
        }

        public boolean isFinished(int[][] board){
                return hasValidMoves(board, BLACK) == false && hasValidMoves(board, WHITE) == false;
        }

        public boolean isInBoard(Pair<Integer, Integer>pos){
                return pos.getLeft() >= 0 && pos.getLeft() < length &&
                        pos.getRight() >= 0 && pos.getRight() < length;
        }

        public int getElementFromBoard(int[][] board, Pair<Integer, Integer>nowPos){
                return board[nowPos.getLeft()][nowPos.getRight()];
        }

        public void setElementInBoard(int[][] board, Pair<Integer, Integer>nowPos, int color){
                board[nowPos.getLeft()][nowPos.getRight()] = color;
        }

        public boolean hasValidMoveInThisDir(int[][] board, int player, int dirIndex, Pair<Integer, Integer>nowPosition){
                //System.out.println("hasValidMoveInThisDir : " + player + " " + dirIndex);
                int numberOfDisksOppositePlayerHas = 0;
                boolean lastDiskBelongsToThisPlayer = false;
                while(true) {
                        // update the x, y coordinate
                        nowPosition.setLeft(nowPosition.getLeft() + directions[dirIndex][0]);
                        nowPosition.setRight(nowPosition.getRight() + directions[dirIndex][1]);

                        /*
                        * If we are out of board now,
                        * check if this is the first iteration, if so then we started with a corner cell, return false
                        * else return the boolean variable
                        * */
                        //loopCounter may be unnecessary here
                        if(isInBoard(nowPosition) == false) {
                                return (lastDiskBelongsToThisPlayer && numberOfDisksOppositePlayerHas > 0);
                        }

                        /*
                        * If this position is empty
                        * return 'ret' (according to last cell)
                        * */
                        if(getElementFromBoard(board, nowPosition) == NONE){
                                return (lastDiskBelongsToThisPlayer && numberOfDisksOppositePlayerHas > 0);
                        }

                        /*
                        * First get the element in this cell
                        * If the element is not possessed by the current player, set 'ret' to false
                        * Otherwise, set 'ret' to true
                        * */

                        /*
                        * Counting number of disks the opposite player has is necessary
                        * Because when we return a true, it must have a non-zero value
                        * */

                        int color = getElementFromBoard(board, nowPosition);

                        if(color != player) {
                                numberOfDisksOppositePlayerHas++;
                                lastDiskBelongsToThisPlayer = false;
                        }
                        else {
                                return (numberOfDisksOppositePlayerHas > 0);
                        }
                }
        }

        public ArrayList<Pair<Integer, Integer>> validMoves(int[][] board, int player){

                ArrayList<Pair<Integer, Integer>>ret = new ArrayList<>();

                for(int row = 0; row < length; ++row){
                        for(int col = 0; col < length; ++col){

                                if(board[row][col] == NONE){
                                        for(int dir = 0; dir < directions.length; ++dir){

                                                if(hasValidMoveInThisDir(board, player, dir, new Pair(row, col))){
                                                        ret.add(new Pair(row, col));
                                                }
                                        }
                                }
                        }
                }
                return ret;
        }

        public void changePlayer(){
                if(nowPlayer == BLACK)
                        nowPlayer = WHITE;
                else
                        nowPlayer = BLACK;
        }

        public Pair<Integer, Integer> getMovePosition(){

                Scanner scanner = new Scanner(System.in);
                Pair now = new Pair();

                System.out.print("turn : ");
                if(nowPlayer == BLACK)
                        System.out.println("BLACK");
                else
                        System.out.println("WHITE");

                System.out.print("enter row : ");
                now.setLeft(scanner.nextInt());
                System.out.print("enter col : ");
                now.setRight(scanner.nextInt());

                return now;
        }

        public void playGame() throws InterruptedException {
                nowPlayer = WHITE;
                initializeGameStats();
                while(isFinished(board) == false){

                        changePlayer();

                        printBoard(nowPlayer);

                        if(hasValidMoves(board, nowPlayer) == false){
                                System.out.println(playerToString(nowPlayer) + " doesn't have a valid move now");
                                //Thread.sleep(2000);
                                continue;
                        }


                        if(checkIfThisPlayerIsComputer(nowPlayer)){

                                //Pair<Pair<Integer, Integer>, Integer> result = this.minimax.minimaxFunc(board, 0, nowPlayer);
                                //Pair<Pair<Integer, Integer>, Integer> result = this.minimax.minimaxWithAlphaBeta(board, 0, nowPlayer, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
                                Pair<Pair<Integer, Integer>, Integer> result = this.minimax.iterativeDeepeningDepthFirstSearchWithAlphaBetaPruning(board, nowPlayer, 500);

                                Pair desiredMove = new Pair(result.getLeft());

                                makeMove(board, (int)desiredMove.getLeft(), (int)desiredMove.getRight(), nowPlayer);

                                System.out.println("Computer plays " + playerToString(nowPlayer));
                                System.out.print("Desired move from computer is : ");
                                desiredMove.print();
                        }
                        else{
                                Pair desiredMove;
                                while(true){
                                        desiredMove = getMovePosition();
                                        if(isValid(board, (int)desiredMove.getLeft(), (int)desiredMove.getRight(), nowPlayer) == true)
                                                break;
                                        System.out.println("your input is not valid");
                                }

                                makeMove(board, (int)desiredMove.getLeft(), (int)desiredMove.getRight(), nowPlayer);
                        }
                        printBoard(nowPlayer);
                }
                finalizeGameStats();
        }

        private String playerToString(int nowPlayer) {
                if(nowPlayer == WHITE)
                        return "WHITE";
                else
                        return "BLACK";
        }

        private void initializeGameStats() {
                startTime = System.currentTimeMillis();
        }

        private void finalizeGameStats() {
                long endTime   = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                System.out.println("Total time needed : " + totalTime / 1000.0 + "s");

                int numWhites = 0, numBlacks = 0;
                for (int i = 0; i < this.board.length; i++) {
                        for (int j = 0; j < this.board.length; j++) {
                                if (this.board[i][j] == WHITE) {
                                        numWhites++;
                                } else if (this.board[i][j] == BLACK) {
                                        numBlacks++;
                                }
                        }
                }
                if(numWhites > numBlacks){
                        System.out.println("Winner is WHITE");
                }
                else if(numBlacks > numWhites){
                        System.out.println("Winner is BLACK");
                }
                else{
                        System.out.println("The game is drawn");
                }
        }


        /**
         * This method prints the board to the console
         * @param turn current turn
         */
        public void printBoard(int turn) {
                int numBlacks = 0;
                int numWhites = 0;
                System.out.println();
                System.out.printf("   ");
                for (int i = 0; i < this.board.length; i++) {
                        System.out.printf(" " + i + "  ");
                }
                System.out.printf("\n  ");
                for (int i = 0; i < this.board.length; i++) {
                        System.out.printf("----");
                }
                System.out.println();
                for (int i = 0; i < this.board.length; i++) {
                        System.out.printf(i + " |");
                        for (int j = 0; j < this.board.length; j++) {
                                if (this.board[i][j] == WHITE) {
                                        System.out.printf(" O |");
                                        numWhites++;
                                }
                                else if (this.board[i][j] == BLACK) {
                                        System.out.printf(" X |");
                                        numBlacks++;
                                }
                                else if (isValid(board, i, j, turn)) {
                                        System.out.printf(" * |");
                                }
                                else {
                                        System.out.printf("   |");
                                }
                        }
                        System.out.println();
                        System.out.printf("  ");
                        for (int j = 0; j < this.board.length; j++) {
                                System.out.printf("----");
                        }
                        System.out.println();

                }
                System.out.println("Black: " + numBlacks + " - " + "White: " + numWhites);
                System.out.println();
        }

        /*
        * 1. player vs player
        * 2. player vs computer
        * 3. computer vs computer
        * */
        public void setMode(String[] args){
                if(args.length > 0){
                        if(args[0].equals("1")){
                                isThisPlayerComputer[0] = false;
                                isThisPlayerComputer[1] = false;
                        }
                        else if(args[0].equals("2")){
                                isThisPlayerComputer[0] = false;
                                isThisPlayerComputer[1] = true;
                        }
                        else if(args[0].equals("3")){
                                isThisPlayerComputer[0] = true;
                                isThisPlayerComputer[1] = true;
                        }
                }
                else{
                        System.out.println("enter a mode to play");
                }
        }

        /*
        * @input: BLACK or WHITE
        * */
        public boolean checkIfThisPlayerIsComputer(int player){
                return isThisPlayerComputer[player - 1];
        }

        /**
         * Main method to run a round of othello
         * @param args command line parameters
         */
        public static void main(String[] args) throws InterruptedException {
                Othello game = new Othello(8);
                minimax = new Minimax(game.board, game);
                game.setMode(args);
                game.playGame();
        }


}