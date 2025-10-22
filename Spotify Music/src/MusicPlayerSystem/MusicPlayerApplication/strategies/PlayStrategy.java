package MusicPlayerSystem.MusicPlayerApplication.strategies;

import MusicPlayerSystem.MusicPlayerApplication.models.*;

public interface PlayStrategy {
    void setPlaylist(Playlist playlist);
    Song next();
    boolean hasNext();
    Song previous();
    boolean hasPrevious();
    default void addToNext(Song song) {}
}
