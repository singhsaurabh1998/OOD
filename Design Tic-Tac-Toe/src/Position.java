public class Position {
    int row, col;
    Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}