
import java.util.*;

public class PlaySnakeAndLadderClient {
    public static void main(String[] args) {
        Dice dice = new Dice(11);
        Player p1 = new Player("Saurabh", 1);
        Player p2 = new Player("Sunda", 2);
        Player p3 = new Player("R Kay", 3);

        Map<String, Integer> playersCurrentPosition = new HashMap<>();
        playersCurrentPosition.put(p1.getPlayerName(), p1.getCurrentPosition());
        playersCurrentPosition.put(p2.getPlayerName(), p2.getCurrentPosition());
        playersCurrentPosition.put(p3.getPlayerName(), p3.getCurrentPosition());

        Queue<Player> allPlayers = new LinkedList<>();
        allPlayers.offer(p1);
        allPlayers.offer(p2);
        allPlayers.offer(p3);

        Jumper snake1 = new Jumper(10, 2,JumperType.SNAKE);
        Jumper snake2 = new Jumper(99, 12,JumperType.SNAKE);
        List<Jumper> snakes = new ArrayList<>();
        snakes.add(snake1);
        snakes.add(snake2);
        Jumper ladder1 = new Jumper(5, 25,JumperType.LADDER);
        Jumper ladder2 = new Jumper(40, 89,JumperType.LADDER);
        List<Jumper> ladders = new ArrayList<>();
        ladders.add(ladder1);
        ladders.add(ladder2);

        GameBoard gb = new GameBoard(dice, allPlayers, snakes, ladders, playersCurrentPosition, 100);
        gb.startGame();
    }
}