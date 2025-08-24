import java.util.List;
import java.util.Map;
import java.util.Queue;

class GameBoard {
    private Dice dice;
    private Queue<Player> nextTurn;
    private List<Jumper> snakes;
    private List<Jumper> ladders;
    private Map<String, Integer> playersCurrentPosition;
    private int boardSize;

    GameBoard(Dice dice, Queue<Player> nextTurn, List<Jumper> snakes, List<Jumper> ladders, Map<String, Integer> playersCurrentPosition, int boardSize) {
        this.dice = dice;
        this.nextTurn = nextTurn;
        this.snakes = snakes;
        this.ladders = ladders;
        this.playersCurrentPosition = playersCurrentPosition;
        this.boardSize = boardSize;
    }

    void startGame() {
        while (nextTurn.size() > 1) {
            Player player = nextTurn.poll();
            String playerName = player.getPlayerName();
            int currentPosition = playersCurrentPosition.get(playerName);
            int diceValue = dice.rollDice();

            int nextCell = currentPosition + diceValue;

            // If move exceeds board size, skip turn
            if (nextCell > boardSize) {
                nextTurn.offer(player);
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
                nextTurn.offer(player);
            }
        }
    }
}