package MusicPlayerSystem.MusicPlayerApplication.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Playlist class representing a music playlist.
 * It contains a name and a list of songs.
 */
public class Playlist {
    private final String playlistName;
    private final List<Song> songList;

    public Playlist(String name) {
        this.playlistName = name;
        this.songList = new ArrayList<>();
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public List<Song> getSongs() {
        return songList;
    }

    public int getSize() {
        return songList.size();
    }

    public void addSongToPlaylist(Song song) {
        if (song == null) {
            throw new RuntimeException("Cannot add null song to playlist.");
        }
        songList.add(song);
    }
}
