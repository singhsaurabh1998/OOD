import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class TicTacToeGameFacade {
    private final Deque<Player> players;
    private final Board board;
    private final WinnerStrategy winnerStrategy;
    private final MoveProvider moveProvider;

    TicTacToeGameFacade(int size, List<Player> players,
                        WinnerStrategy winnerStrategy,
                        MoveProvider moveProvider) {
        this.board = new Board(size);
        this.players = new LinkedList<>(players);
        this.winnerStrategy = winnerStrategy;
        this.moveProvider = moveProvider;
    }

    public GameResult startGame() {
        while (true) {
            Player current = players.removeFirst();
            board.printBoard();
            List<Position> freeSpaces = board.getFreeCells();

            if (freeSpaces.isEmpty())
                return new GameResult(true, null);

            Position move = moveProvider.getNextMove(current, board);
            boolean success = board.addPiece(move.row, move.col, current.pieceType);

            if (!success) {
                System.out.println("Invalid move, try again.");
                players.addFirst(current); // retry
                continue;
            }

            if (winnerStrategy.checkWinner(board, move, current.pieceType)) {
                board.printBoard();
                return new GameResult(false, current);
            }
            players.addLast(current);
        }
    }
}
