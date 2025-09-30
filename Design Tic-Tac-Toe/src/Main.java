import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Player p1 = new Player("Player1", new PlayingPiece(PieceType.X));
        Player p2 = new Player("Player2", new PlayingPiece(PieceType.O));

        TicTacToeGameFacade game = new TicTacToeGameFacade(
                3,
                Arrays.asList(p1, p2),
                new TicTacToeWinnerStrategy(),
                new ConsoleMoveProvider()
        );

        GameResult result = game.startGame();
        if (result.tie) System.out.println("Game ended in a tie!");
        else System.out.println("Winner is: " + result.winner.name);
    }
}