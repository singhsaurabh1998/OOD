package MusicPlayerSystem.MusicPlayerApplication.core;


import MusicPlayerSystem.MusicPlayerApplication.device.IAudioOutputDevice;
import MusicPlayerSystem.MusicPlayerApplication.models.Song;

/**
 * AudioEngine class manages the playback of songs. It can play, pause, and resume songs.
 * It interacts with an IAudioOutputDevice to output audio.
 * The class keeps track of the current song and its playback state (playing or paused).
 */
public class AudioEngine {
    private Song currentSong;
    private boolean songIsPaused;

    public AudioEngine() {
        currentSong = null;
        songIsPaused = false;
    }

    public String getCurrentSongTitle() {
        if (currentSong != null) {
            return currentSong.getTitle();
        }
        return "";
    }

    public boolean isPaused() {
        return songIsPaused;
    }

    /**
     * Play a song using the provided audio output device
     * If the same song is paused, it resumes playback
     * Otherwise, it starts playing the new song
     */
    public void play(IAudioOutputDevice aod, Song song) {
        if (song == null) {
            throw new RuntimeException("Cannot play a null song.");
        }
        // Resume if same song was paused
        if (songIsPaused && song == currentSong) {
            songIsPaused = false;
            System.out.println("Resuming song: " + song.getTitle());
            aod.playAudio(song);
            return;
        }

        currentSong = song;
        songIsPaused = false;
        System.out.println("Playing song: " + song.getTitle());
        aod.playAudio(song);
    }

    public void pause() {
        if (currentSong == null) {
            throw new RuntimeException("No song is currently playing to pause.");
        }
        if (songIsPaused) {
            throw new RuntimeException("Song is already paused.");
        }
        songIsPaused = true;
        System.out.println("Pausing song: " + currentSong.getTitle());
    }
}
