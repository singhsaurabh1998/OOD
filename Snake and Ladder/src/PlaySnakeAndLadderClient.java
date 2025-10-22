
import java.util.*;

class Facade {
    public GameBoard initialise(int numberOfDice, List<Player> players) {
        Dice dice = new Dice(numberOfDice);

        Map<String, Integer> playersCurrentPosition = new HashMap<>();
        Player p1 = players.get(0);
        Player p2 = players.get(1);
        playersCurrentPosition.put(p1.getPlayerName(), p1.getCurrentPosition());
        playersCurrentPosition.put(p2.getPlayerName(), p2.getCurrentPosition());

        Queue<Player> allPlayers = new LinkedList<>();
        allPlayers.offer(p1);
        allPlayers.offer(p2);

        // Setting up snakes and ladders
        Jumper snake1 = new Jumper(10, 2, JumperType.SNAKE);
        Jumper snake2 = new Jumper(99, 12, JumperType.SNAKE);
        List<Jumper> snakes = new ArrayList<>();
        snakes.add(snake1);
        snakes.add(snake2);

        Jumper ladder1 = new Jumper(5, 25, JumperType.LADDER);
        Jumper ladder2 = new Jumper(40, 89, JumperType.LADDER);
        List<Jumper> ladders = new ArrayList<>();
        ladders.add(ladder1);
        ladders.add(ladder2);
        GameBoard gb = new GameBoard(dice, allPlayers, snakes, ladders, playersCurrentPosition, 100);
        return gb;
    }
}

public class PlaySnakeAndLadderClient {
    public static void main(String[] args) throws InterruptedException {
        List<Player> players = new ArrayList<>();
        Facade facade = new Facade();
        Player p1 = new Player("Saurabh", 1);
        Player p2 = new Player("Sunda", 2);
        players.add(p1);
        players.add(p2);

        GameBoard gb = facade.initialise(1, players);
        gb.startGame();
    }
}