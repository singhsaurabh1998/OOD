package MusicPlayerSystem.MusicPlayerApplication;

import MusicPlayerSystem.MusicPlayerApplication.enums.DeviceType;
import MusicPlayerSystem.MusicPlayerApplication.enums.PlayStrategyType;
import MusicPlayerSystem.MusicPlayerApplication.managers.PlaylistManager;
import MusicPlayerSystem.MusicPlayerApplication.models.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerApplication {
    private static MusicPlayerApplication instance = null;
    private final List<Song> songLibrary;

    private MusicPlayerApplication() {
        songLibrary = new ArrayList<>();
    }

    public static synchronized MusicPlayerApplication getInstance() {
        if (instance == null) {
            instance = new MusicPlayerApplication();
        }
        return instance;
    }

    public void createSongInLibrary(String title, String artist) {
        Song newSong = new Song(title, artist);
        songLibrary.add(newSong);
    }

    public Song findSongByTitle(String title) {
        for (Song s : songLibrary) {
            if (s.getTitle().equals(title)) {
                return s;
            }
        }
        return null;
    }

    public void createPlaylist(String playlistName) {
        PlaylistManager.getInstance().createPlaylist(playlistName);
    }

    public void addSongToPlaylist(String playlistName, String songTitle) {
        Song song = findSongByTitle(songTitle);
        if (song == null) {
            throw new RuntimeException("Song \"" + songTitle + "\" not found in library.");
        }
        PlaylistManager.getInstance().addSongToPlaylist(playlistName, song);
    }

    public void connectAudioDevice(DeviceType deviceType) {
        MusicPlayerFacade.getInstance().connectDevice(deviceType);
    }

    public void selectPlayStrategy(PlayStrategyType strategyType) {
        MusicPlayerFacade.getInstance().setPlayStrategy(strategyType);
    }

    public void loadPlaylist(String playlistName) {
        MusicPlayerFacade.getInstance().loadPlaylist(playlistName);
    }

    public void playSingleSong(String songTitle) {
        Song song = findSongByTitle(songTitle);
        if (song == null) {
            throw new RuntimeException("Song \"" + songTitle + "\" not found.");
        }
        MusicPlayerFacade.getInstance().playSong(song);
    }

    public void pauseCurrentSong(String songTitle) {
        Song song = findSongByTitle(songTitle);
        if (song == null) {
            throw new RuntimeException("Song \"" + songTitle + "\" not found.");
        }
        MusicPlayerFacade.getInstance().pauseSong(song);
    }

    public void playAllTracksInPlaylist() {
        MusicPlayerFacade.getInstance().playAllTracks();
    }

    public void playPreviousTrackInPlaylist() {
        MusicPlayerFacade.getInstance().playPreviousTrack();
    }

    public void queueSongNext(String songTitle) {
        Song song = findSongByTitle(songTitle);
        if (song == null) {
            throw new RuntimeException("Song \"" + songTitle + "\" not found.");
        }
        MusicPlayerFacade.getInstance().enqueueNext(song);
    }
}
