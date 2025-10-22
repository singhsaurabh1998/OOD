package MusicPlayerSystem.MusicPlayerApplication;

import MusicPlayerSystem.MusicPlayerApplication.enums.DeviceType;
import MusicPlayerSystem.MusicPlayerApplication.enums.PlayStrategyType;

public class Main {
    public static void main(String[] args) {
        MusicPlayerApplication application = MusicPlayerApplication.getInstance();

        // Populate library
        application.createSongInLibrary("Kesariya", "Arijit Singh");
        application.createSongInLibrary("Chaiyya Chaiyya", "Sukhwinder Singh");
        application.createSongInLibrary("Tum Hi Ho", "Arijit Singh");
        application.createSongInLibrary("Jai Ho", "A. R. Rahman");
        application.createSongInLibrary("Zinda", "Siddharth Mahadevan");

        // Create playlist and add songs
        application.createPlaylist("Bollywood Vibes");
        application.addSongToPlaylist("Bollywood Vibes", "Kesariya");
        application.addSongToPlaylist("Bollywood Vibes", "Chaiyya Chaiyya");
        application.addSongToPlaylist("Bollywood Vibes", "Tum Hi Ho");
        application.addSongToPlaylist("Bollywood Vibes", "Jai Ho");

        // Connect device
        application.connectAudioDevice(DeviceType.BLUETOOTH);

        //Play/pause a single song
        application.playSingleSong("Zinda");
        application.pauseCurrentSong("Zinda");
        application.playSingleSong("Zinda");  // resume

        System.out.println("\n-- Sequential Playback --\n");
        application.selectPlayStrategy(PlayStrategyType.SEQUENTIAL);
        application.loadPlaylist("Bollywood Vibes");
        application.playAllTracksInPlaylist();

        System.out.println("\n-- Random Playback --\n");
        application.selectPlayStrategy(PlayStrategyType.RANDOM);
        application.loadPlaylist("Bollywood Vibes");
        application.playAllTracksInPlaylist();

        System.out.println("\n-- Custom Queue Playback --\n");
        application.selectPlayStrategy(PlayStrategyType.CUSTOM_QUEUE);
        application.loadPlaylist("Bollywood Vibes");
        application.queueSongNext("Kesariya");
        application.queueSongNext("Tum Hi Ho");
        application.playAllTracksInPlaylist();

        System.out.println("\n-- Play Previous in Sequential --\n");
        application.selectPlayStrategy(PlayStrategyType.SEQUENTIAL);
        application.loadPlaylist("Bollywood Vibes");
        application.playAllTracksInPlaylist();

        application.playPreviousTrackInPlaylist();
        application.playPreviousTrackInPlaylist();

    }
}
