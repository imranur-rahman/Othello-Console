import java.util.ArrayList;
import java.util.Scanner;

public class Othello {

        public static final int NONE = 0;
        public static final int BLACK = 1;
        public static final int WHITE = 2;

        private static int[][] directions = new int[][]{{-1,-1}, {-1,0}, {-1,1},  {0,1}, {1,1},  {1,0},  {1,-1},  {0, -1}};

        public int[][] board;
        public int length;

        public int nowPlayer = BLACK;

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

        public boolean isValid(int row, int col, int player) {
                /*
                * If this position has a valid move in any direction for this player
                * then this position is valid for this player
                * */
                for(int dirIndex = 0; dirIndex < directions.length; ++dirIndex){
                        if(hasValidMoveInThisDir(player, dirIndex, new Pair(row, col)))
                                return true;
                }
                return false;
        }

        public boolean hasValidMoves(int player) {
                ArrayList<Pair<Integer, Integer>> moves = validMoves(player);
                return (moves.size() != 0);
        }

        public void makeMove(int row, int col, int player) {
                for(int dirIndex = 0; dirIndex < directions.length; ++dirIndex){
                        if(hasValidMoveInThisDir(player, dirIndex, new Pair(row, col)))
                                flipInThisDirection(player, dirIndex, new Pair(row, col));
                }
                setElementInBoard(new Pair(row, col), player);
        }

        public void flipInThisDirection(int player, int dirIndex, Pair<Integer, Integer>nowPosition){
                System.out.println("flipInThisDirection : " + player + " " + dirIndex);
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
                        * return 'ret', our job is done
                        * */
                        if(getElementFromBoard(nowPosition) == NONE){
                                return;
                        }

                        /*
                        * First get the element in this cell
                        * If the element is not possessed by the current player
                        * make it possessed by the current player
                        * */
                        if(getElementFromBoard(nowPosition) != player)
                                setElementInBoard(nowPosition, player);

                }
        }

        public boolean isFinished(){
                return hasValidMoves(BLACK) == false && hasValidMoves(WHITE) == false;
        }

        public boolean isInBoard(Pair<Integer, Integer>pos){
                return pos.getLeft() >= 0 && pos.getLeft() < length &&
                        pos.getRight() >= 0 && pos.getRight() < length;
        }

        public int getElementFromBoard(Pair<Integer, Integer>nowPos){
                return board[nowPos.getLeft()][nowPos.getRight()];
        }

        public void setElementInBoard(Pair<Integer, Integer>nowPos, int color){
                board[nowPos.getLeft()][nowPos.getRight()] = color;
        }

        public boolean hasValidMoveInThisDir(int player, int dirIndex, Pair<Integer, Integer>nowPosition){
                //System.out.println("hasValidMoveInThisDir : " + player + " " + dirIndex);
                boolean ret = true;
                int loopCounter = 0;
                int numberOfDisksOppositePlayerHas = 0;
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
                                if(loopCounter == 0)
                                        return false;
                                else
                                        return (ret && numberOfDisksOppositePlayerHas > 0);
                        }

                        /*
                        * If this position is empty
                        * return 'ret' (according to last cell)
                        * */
                        if(getElementFromBoard(nowPosition) == NONE){
                                return (ret && numberOfDisksOppositePlayerHas > 0);
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

                        int color = getElementFromBoard(nowPosition);
                        if(color != player) {
                                ret = false;
                                numberOfDisksOppositePlayerHas++;
                        }
                        else {
                                ret = true;
                        }

                        loopCounter++;
                }
        }

        public ArrayList<Pair<Integer, Integer>> validMoves(int player){

                ArrayList<Pair<Integer, Integer>>ret = new ArrayList<>();

                for(int row = 0; row < length; ++row){
                        for(int col = 0; col < length; ++col){

                                if(board[row][col] == NONE){
                                        for(int dir = 0; dir < directions.length; ++dir){

                                                if(hasValidMoveInThisDir(player, dir, new Pair(row, col))){
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
                while(isFinished() == false){

                        changePlayer();

                        printBoard(nowPlayer);

                        if(hasValidMoves(nowPlayer) == false){
                                System.out.println("this player doesn't have a valid move now");
                                Thread.sleep(2000);
                                continue;
                        }


                        Pair desiredMove;
                        while(true){
                                desiredMove = getMovePosition();
                                if(isValid((int)desiredMove.getLeft(), (int)desiredMove.getRight(), nowPlayer) == true)
                                        break;
                                System.out.println("your input is not valid");
                        }

                        makeMove((int)desiredMove.getLeft(), (int)desiredMove.getRight(), nowPlayer);
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
                                else if (isValid(i, j, turn)) {
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

        /**
         * Main method to run a round of othello
         * @param args command line parameters
         */
        public static void main(String[] args) throws InterruptedException {
                Othello game = new Othello(8);
                game.playGame();
                //game.printBoard(0);
        }


}