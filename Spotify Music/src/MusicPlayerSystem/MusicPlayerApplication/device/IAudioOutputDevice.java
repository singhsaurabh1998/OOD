package MusicPlayerSystem.MusicPlayerApplication.device;

import MusicPlayerSystem.MusicPlayerApplication.models.Song;

public interface IAudioOutputDevice {
    void playAudio(Song song);
}
