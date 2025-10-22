import java.util.List;
import java.util.Map;
import java.util.Queue;

class GameBoard {
    private final Dice dice;
    private final Queue<Player> players;
    private final List<Jumper> snakes;
    private final List<Jumper> ladders;
    private final Map<String, Integer> playersCurrentPosition;
    private final int boardSize;

    GameBoard(Dice dice, Queue<Player> players, List<Jumper> snakes, List<Jumper> ladders, Map<String, Integer> playersCurrentPosition, int boardSize) {
        this.dice = dice;
        this.players = players;
        this.snakes = snakes;
        this.ladders = ladders;
        this.playersCurrentPosition = playersCurrentPosition;
        this.boardSize = boardSize;
    }

    void startGame() throws InterruptedException {
        while (players.size() > 1) {
            //Thread.sleep(800);
            Player player = players.poll();
            String playerName = player.getPlayerName();
            int currentPosition = playersCurrentPosition.get(playerName);
            int diceValue = dice.rollDice();
            System.out.println(playerName + " rolled a " + diceValue);
            int nextCell = currentPosition + diceValue;

            // If move exceeds board size, skip turn
            if (nextCell > boardSize) {
                players.offer(player);
                continue;
            }

            // Check if the player wins
            if (nextCell == boardSize) {
                System.out.println(playerName + " won the game");
                continue;
            }

            // Check for snake bite
            for (Jumper snake : snakes) {
                if (snake.startPoint == nextCell) {
                    System.out.println(playerName + " bitten by snake at: " + nextCell);
                    nextCell = snake.endPoint;
                    break;
                }
            }

            // Check for ladder climb
            for (Jumper ladder : ladders) {
                if (ladder.startPoint == nextCell) {
                    System.out.println(playerName + " got a ladder at: " + nextCell);
                    nextCell = ladder.endPoint;
                    break;
                }
            }

            // Final position check
            if (nextCell == boardSize) {
                System.out.println(playerName + " won the game");
            } else {
                playersCurrentPosition.put(playerName, nextCell);
                System.out.println(playerName + " is at position " + nextCell);
                players.offer(player);
            }
        }
    }
}