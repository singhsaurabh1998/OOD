import java.util.*;

public class PlaySnakeAndLadderClient {
    public static void main(String[] args) throws InterruptedException {
        List<Player> players = new ArrayList<>();
        Facade facade = new Facade();
        Player p1 = new Player("Saurabh", 1);
        Player p2 = new Player("Amit", 2);
        players.add(p1);
        players.add(p2);

        GameBoard gb = facade.initialise(1, players);
        gb.startGame();
    }
}