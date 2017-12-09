import java.util.ArrayList;

public class Minimax {

        private int[][] board;
        private Othello othello;

        private int maxDepth = 8;


        /**
         * Clones the provided array
         *
         * @param src
         * @return a new clone of the provided array
         */
        public static int[][] cloneArray(int[][] src) {
                int length = src.length;
                int[][] target = new int[length][src[0].length];
                for (int i = 0; i < length; i++) {
                        System.arraycopy(src[i], 0, target[i], 0, src[i].length);
                }
                return target;
        }


        public int changeThisPlayer(int player){
                if(player == Othello.BLACK)
                        return Othello.WHITE;
                else
                        return Othello.BLACK;
        }


        public int utility(int[][] board, int player){
                if(player == Othello.BLACK)
                        return heuristic2(board, player);
                else
                        return heuristic3(board, player);
        }


        /*
        * Positional Heuristic
        * Static weight associated to each board position
        * The idea is to maximize its own valuable positions and
        * Minimize its opponents valuable positions.
        * */
        public int heuristic1(int[][] board, int player){
                if(board.length != 8  ||  board[0].length != 8){
                        System.out.println("8x8 board needed");
                        return 0;
                }
                int ret = 0;
                int[][] weight = new int[][]{
                        { 100, -20, 10,  5,  5, 10, -20, 100 },
                        { -20, -50, -2, -2, -2, -2, -50, -20 },
                        {  10,  -2, -1, -1, -1, -1,  -2,  10 },
                        {   5,  -2, -1, -1, -1, -1,  -2,   5 },
                        {   5,  -2, -1, -1, -1, -1,  -2,   5 },
                        {  10,  -2, -1, -1, -1, -1,  -2,  10 },
                        { -20, -50, -2, -2, -2, -2, -50, -20 },
                        { 100, -20, 10,  5,  5, 10, -20, 100 }
                };
                for(int row = 0; row < board.length; ++row) {
                        for (int col = 0; col < board[row].length; ++col) {
                                if (board[row][col] == Othello.NONE)
                                        continue;
                                if(board[row][col] == player)
                                        ret += weight[row][col];
                                else
                                        ret -= weight[row][col];
                        }
                }
                return ret;
        }

        /*
        * Mobility Heuristic
        * It refers to the number of legal moves a player can make in a particular position.
        * The idea is to maximize its own mobility and minimize its opponent's mobility.
        * Corner squares are important here.
        * */
        public int heuristic2(int[][] board, int player){
                int numberOfCornerCellThisPlayerHas = 0;
                int numberOfCornerCellOpponentPlayerHas = 0;
                int otherCellsThisPlayerHas = 0;
                int otherCellsOpponentPlayerHas = 0;

                int weightForCornerCell = 10;
                int weightForOtherCell = 1;

                for(int row = 0; row < board.length; ++row) {
                        for (int col = 0; col < board[row].length; ++col) {
                                if (board[row][col] == Othello.NONE)
                                        continue;
                                if(row == 0  ||  col == 0){
                                        if(board[row][col] == player)
                                                numberOfCornerCellThisPlayerHas++;
                                        else
                                                numberOfCornerCellOpponentPlayerHas++;
                                }
                                else{
                                        if(board[row][col] == player)
                                                otherCellsThisPlayerHas++;
                                        else
                                                otherCellsOpponentPlayerHas++;
                                }
                        }
                }
                //int division kortechi, float kora uchit
                return weightForCornerCell * (numberOfCornerCellThisPlayerHas - numberOfCornerCellOpponentPlayerHas) +
                        weightForOtherCell * (otherCellsThisPlayerHas - otherCellsOpponentPlayerHas) /
                                (otherCellsThisPlayerHas + otherCellsOpponentPlayerHas);
        }

        /*
        * Absolute Count heuristic
        * The idea is to maximize number of one's own disks and
        * Minimize the number of opponent player's disks.
        * */
        public int heuristic3(int[][] board, int player){
                int numberOfOwnDisks = 0;
                int numberOfOpponentDisks = 0;
                for(int row = 0; row < board.length; ++row) {
                        for (int col = 0; col < board[row].length; ++col) {
                                if(board[row][col] == Othello.NONE)
                                        continue;

                                if(board[row][col] == player)
                                        numberOfOwnDisks++;
                                else
                                        numberOfOpponentDisks++;
                        }
                }
                return numberOfOwnDisks - numberOfOpponentDisks;
        }

        public Pair<Pair<Integer, Integer>, Integer> minimaxFunc(int[][] board, int depth, int player){
                if(depth == maxDepth)
                        return new Pair(new Pair(), utility(board, player));
                else{
                        ArrayList<Pair<Integer, Integer>> moves = othello.validMoves(board, player);
                        if(moves.size() == 0)
                                return new Pair(new Pair(), utility(board, player));
                        else{
                                int bestScore = Integer.MIN_VALUE;
                                Pair<Integer, Integer> bestMove = new Pair();
                                for(Pair<Integer, Integer>nowPos: moves){
                                        int[][] tempBoard = cloneArray(board);
                                        othello.makeMove(tempBoard, nowPos.getLeft(), nowPos.getRight(), player);
                                        Pair<Pair<Integer, Integer>, Integer> found = minimaxFunc(tempBoard, depth + 1, player);
                                        if(found.getRight() > bestScore){
                                                bestScore = found.getRight();
                                                bestMove = new Pair(nowPos);
                                        }
                                }
                                return new Pair(bestMove, bestScore);
                        }
                }
        }

        public Pair<Pair<Integer, Integer>, Integer> minimaxWithAlphaBeta(int[][] board, int depth, int player, boolean isMaximizingPlayer, int alpha, int beta){
                if(depth == maxDepth)
                        return new Pair(new Pair(), utility(board, player));

                ArrayList<Pair<Integer, Integer>> moves = othello.validMoves(board, player);
                if(moves.size() == 0)
                        return new Pair(new Pair(), utility(board, player));

                if(isMaximizingPlayer){
                        int bestScore = Integer.MIN_VALUE;
                        Pair<Integer, Integer> bestMove = new Pair();
                        for(Pair<Integer, Integer>nowPos: moves){
                                int[][] tempBoard = cloneArray(board);
                                othello.makeMove(tempBoard, nowPos.getLeft(), nowPos.getRight(), player);
                                Pair<Pair<Integer, Integer>, Integer> found = minimaxWithAlphaBeta(tempBoard, depth + 1, changeThisPlayer(player), false, alpha, beta);
                                if(found.getRight() > bestScore){
                                        bestScore = found.getRight();
                                        bestMove = new Pair(nowPos);
                                }
                                if(bestScore > alpha){
                                        alpha = bestScore;
                                }

                                /*
                                * If alpha is greater than beta, return alpha
                                * */
                                if(alpha >= beta){
                                        break;
                                }

                        }
                        return new Pair(bestMove, bestScore);
                }
                else{
                        int bestScore = Integer.MAX_VALUE;
                        Pair<Integer, Integer> bestMove = new Pair();
                        for(Pair<Integer, Integer>nowPos: moves){
                                int[][] tempBoard = cloneArray(board);
                                othello.makeMove(tempBoard, nowPos.getLeft(), nowPos.getRight(), player);
                                Pair<Pair<Integer, Integer>, Integer> found = minimaxWithAlphaBeta(tempBoard, depth + 1, changeThisPlayer(player), true, alpha, beta);
                                if(found.getRight() < bestScore){
                                        bestScore = found.getRight();
                                        bestMove = new Pair(nowPos);
                                }
                                if(bestScore < beta){
                                        beta = bestScore;
                                }

                                /*
                                * If beta is less than alpha, return beta
                                * */
                                if(beta <= alpha){
                                        break;
                                }

                        }
                        return new Pair(bestMove, bestScore);
                }
        }

        public int getMaxDepth() {
                return maxDepth;
        }

        public void setMaxDepth(int maxDepth) {
                this.maxDepth = maxDepth;
        }

        public Minimax(int[][] board, Othello othello){
                this.board = board;
                this.othello = othello;
        }
}
