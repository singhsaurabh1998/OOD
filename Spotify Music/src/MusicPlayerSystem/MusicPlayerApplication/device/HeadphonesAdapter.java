package MusicPlayerSystem.MusicPlayerApplication.device;


import MusicPlayerSystem.MusicPlayerApplication.external.HeadphonesAPI;
import MusicPlayerSystem.MusicPlayerApplication.models.Song;

public class HeadphonesAdapter implements IAudioOutputDevice {
    private final HeadphonesAPI headphonesApi;

    public HeadphonesAdapter(HeadphonesAPI api) {
        this.headphonesApi = api;
    }

    @Override
    public void playAudio(Song song) {
        String payload = song.getTitle() + " by " + song.getArtist();
        headphonesApi.playSoundViaJack(payload);
    }
}
