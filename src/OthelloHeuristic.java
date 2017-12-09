public class OthelloHeuristic {
        /*
        * Positional Heuristic
        * Static weight associated to each board position
        * The idea is to maximize its own valuable positions and
        * Minimize its opponents valuable positions.
        * */
        public int heuristic1(){
                int ret = 0;
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
}
