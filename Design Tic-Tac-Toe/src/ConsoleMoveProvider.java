import java.util.Scanner;

// Simple MoveProvider that reads moves from the console input and return Position.
public class ConsoleMoveProvider implements MoveProvider {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Position getNextMove(Player player, Board board) {
        System.out.print(player.name + " enter row,col: ");
        String[] parts = scanner.nextLine().split(",");
        return new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
}