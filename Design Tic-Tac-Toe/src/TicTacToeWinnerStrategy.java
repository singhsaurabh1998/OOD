public class TicTacToeWinnerStrategy implements WinnerStrategy {
    @Override
    public boolean checkWinner(Board board, Position lastMove, PieceType pieceType) {
        int row = lastMove.row, col = lastMove.col;
        boolean rowMatch = true, colMatch = true, diagMatch = true, antiDiagMatch = true;

        for (int i = 0; i < board.size; i++) {
            if (board.board[row][i] == null || board.board[row][i].pieceType != pieceType)
                rowMatch = false;
            if (board.board[i][col] == null || board.board[i][col].pieceType != pieceType)
                colMatch = false;
            if (board.board[i][i] == null || board.board[i][i].pieceType != pieceType)
                diagMatch = false;
            if (board.board[i][board.size - i - 1] == null ||
                    board.board[i][board.size - i - 1].pieceType != pieceType)
                antiDiagMatch = false;
        }
        return rowMatch || colMatch || diagMatch || antiDiagMatch;
    }
}