// ====================== STRATEGY: WINNER CHECK ======================
public interface WinnerStrategy {
    boolean checkWinner(Board board, Position lastMove, PieceType pieceType);
}

