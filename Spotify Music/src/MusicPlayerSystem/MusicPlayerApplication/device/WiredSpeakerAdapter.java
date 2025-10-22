package MusicPlayerSystem.MusicPlayerApplication.device;

import MusicPlayerSystem.MusicPlayerApplication.external.WiredSpeakerAPI;
import MusicPlayerSystem.MusicPlayerApplication.models.Song;

public class WiredSpeakerAdapter implements IAudioOutputDevice {
    private final WiredSpeakerAPI wiredApi;

    public WiredSpeakerAdapter(WiredSpeakerAPI api) {
        this.wiredApi = api;
    }

    @Override
    public void playAudio(Song song) {
        String payload = song.getTitle() + " by " + song.getArtist();
        wiredApi.playSoundViaCable(payload);
    }
}
