public class TicTacToeWinnerStrategy implements WinnerStrategy {
    @Override
    public boolean checkWinner(Board board, Position lastMove, PieceType pieceType) {
        int row = lastMove.row, col = lastMove.col;
        boolean rowMatch = true, colMatch = true, diagMatch = true, antiDiagMatch = true;

        for (int i = 0; i < board.size; i++) {
            if (board.board[row][i] == null || board.board[row][i] != pieceType)
                rowMatch = false;
            if (board.board[i][col] == null || board.board[i][col] != pieceType)
                colMatch = false;
            if (board.board[i][i] == null || board.board[i][i] != pieceType)
                diagMatch = false;
            if (board.board[i][board.size - i - 1] == null ||
                    board.board[i][board.size - i - 1] != pieceType)
                antiDiagMatch = false;
        }
        return rowMatch || colMatch || diagMatch || antiDiagMatch;
    }
}