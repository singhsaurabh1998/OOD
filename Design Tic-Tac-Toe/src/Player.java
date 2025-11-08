
/**
 * Player class represents a player in the Tic-Tac-Toe game.
 * Each player has a name and a playing piece (X or O).
 * This class provides methods to access and modify the player's name and playing piece.
 * It is used to manage player information during the game.
 */
public class Player {

    public String name;
    public PieceType pieceType; //X or O which piece the player is using

    public Player(String name, PieceType playingPiece) {
        this.name = name;
        this.pieceType = playingPiece;
    }

    public String getName() {
        return name;
    }
}
