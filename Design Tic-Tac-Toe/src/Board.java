import java.util.ArrayList;
import java.util.List;

/**
 * Board class representing a Tic-Tac-Toe board.
 * It supports adding pieces, retrieving free cells, and printing the board state.
 * The board is represented as a 2D array of PlayingPiece objects.
 * The size of the board is defined at the time of creation.
 */
public class Board {
    int size;
    PlayingPiece[][] board;

    Board(int size) {
        this.size = size;
        this.board = new PlayingPiece[size][size];
    }

    boolean addPiece(int row, int col, PlayingPiece piece) {
        if (row < 0 || row >= size || col < 0 || col >= size) return false;
        if (board[row][col] != null) return false;
        board[row][col] = piece;
        return true;
    }

    List<Position> getFreeCells() {
        List<Position> free = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) free.add(new Position(i, j));
            }
        }
        return free;
    }

    void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == null) System.out.print(" . ");
                else System.out.print(" " + board[i][j].pieceType + " ");
            }
            System.out.println();
        }
    }
}