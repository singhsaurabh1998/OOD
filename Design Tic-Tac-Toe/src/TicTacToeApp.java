//import java.util.Scanner;
//
//enum Symbol {
//    X, O, EMPTY
//}
//
//class Player {
//    private final String name;
//    private final Symbol symbol;
//
//    public Player(String name, Symbol symbol) {
//        this.name = name;
//        this.symbol = symbol;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Symbol getSymbol() {
//        return symbol;
//    }
//}
//
//class Cell {
//    private int row;
//    private int col;
//    private Symbol symbol;
//
//    public Cell(int row, int col) {
//        this.row = row;
//        this.col = col;
//        this.symbol = Symbol.EMPTY;
//    }
//
//    public Symbol getSymbol() {
//        return symbol;
//    }
//
//    public boolean isEmpty() {
//        return symbol == Symbol.EMPTY;
//    }
//
//    public void setSymbol(Symbol symbol) {
//        this.symbol = symbol;
//    }
//}
//
//interface WinnerStrategy {
//    boolean checkWinner(Cell[][] grid, Symbol symbol);
//}
//
//class NormalWinnerStrategy implements WinnerStrategy {
//    @Override
//    public boolean checkWinner(Cell[][] grid, Symbol symbol) {
//        int SIZE = grid.length;
//        for (int i = 0; i < SIZE; i++) {
//            if (grid[i][0].getSymbol() == symbol &&
//                    grid[i][1].getSymbol() == symbol &&
//                    grid[i][2].getSymbol() == symbol) return true;
//        }
//        // cols
//        for (int j = 0; j < SIZE; j++) {
//            if (grid[0][j].getSymbol() == symbol &&
//                    grid[1][j].getSymbol() == symbol &&
//                    grid[2][j].getSymbol() == symbol) return true;
//        }
//        // diagonals
//        if (grid[0][0].getSymbol() == symbol &&
//                grid[1][1].getSymbol() == symbol &&
//                grid[2][2].getSymbol() == symbol) return true;
//
//        if (grid[0][2].getSymbol() == symbol &&
//                grid[1][1].getSymbol() == symbol &&
//                grid[2][0].getSymbol() == symbol) return true;
//
//        return false;
//    }
//}
//
//class Board {
//    private final int SIZE = 3;
//    private final Cell[][] grid;
//    WinnerStrategy winnerStrategy;
//
//    public Board(WinnerStrategy winnerStrategy) {
//        grid = new Cell[SIZE][SIZE];
//        for (int i = 0; i < SIZE; i++) {
//            for (int j = 0; j < SIZE; j++) {
//                grid[i][j] = new Cell(i, j);
//            }
//        }
//        this.winnerStrategy = winnerStrategy;
//    }
//
//    public boolean markCell(int row, int col, Symbol symbol) {
//        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) return false;
//        if (!grid[row][col].isEmpty()) return false;
//        grid[row][col].setSymbol(symbol);
//        return true;
//    }
//
//    public boolean isFull() {
//        for (int i = 0; i < SIZE; i++) {
//            for (int j = 0; j < SIZE; j++) {
//                if (grid[i][j].isEmpty()) return false;
//            }
//        }
//        return true;
//    }
//
//    public boolean checkWinner(Symbol symbol) {
//        return winnerStrategy.checkWinner(grid, symbol);
//
//    }
//
//    public void printBoard() {
//        for (int i = 0; i < SIZE; i++) {
//            for (int j = 0; j < SIZE; j++) {
//                switch (grid[i][j].getSymbol()) {
//                    case X:
//                        System.out.print(" X ");
//                        break;
//                    case O:
//                        System.out.print(" O ");
//                        break;
//                    default:
//                        System.out.print(" - ");
//                }
//            }
//            System.out.println();
//        }
//    }
//
//    public Cell[][] getGrid() {
//        return grid;
//    }
//}
//
//class Game {
//    private final Board board;
//    private final Player player1;
//    private final Player player2;
//    private Player currentTurn;
//
//    public Game(String p1, String p2) {
//        this.board = new Board(new NormalWinnerStrategy());
//        this.player1 = new Player(p1, Symbol.X);
//        this.player2 = new Player(p2, Symbol.O);
//        this.currentTurn = player1;
//    }
//
//    public void play() {
//        Scanner sc = new Scanner(System.in);
//
//        while (true) {
//            board.printBoard();
//            System.out.println(currentTurn.getName() + "'s turn. Enter row and col (0-2): ");
//            int row = sc.nextInt();
//            int col = sc.nextInt();
//
//            if (!board.markCell(row, col, currentTurn.getSymbol())) {
//                System.out.println("Invalid move, try again.");
//                continue;
//            }
//
//            if (board.checkWinner(currentTurn.getSymbol())) {
//                board.printBoard();
//                System.out.println(currentTurn.getName() + " wins!");
//                break;
//            }
//
//            if (board.isFull()) {
//                board.printBoard();
//                System.out.println("It's a draw!");
//                break;
//            }
//
//            currentTurn = (currentTurn == player1) ? player2 : player1;
//        }
//
//        sc.close();
//    }
//}
//
//public class TicTacToeApp {
//    public static void main(String[] args) {
//        Game game = new Game("Alice", "Bob");
//        game.play();
//    }
//}
