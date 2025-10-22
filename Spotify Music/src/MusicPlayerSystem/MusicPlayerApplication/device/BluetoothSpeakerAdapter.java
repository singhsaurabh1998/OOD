package MusicPlayerSystem.MusicPlayerApplication.device;
import MusicPlayerSystem.MusicPlayerApplication.external.BluetoothSpeakerAPI;
import MusicPlayerSystem.MusicPlayerApplication.models.Song;

public class BluetoothSpeakerAdapter implements IAudioOutputDevice {
    private final BluetoothSpeakerAPI bluetoothApi;

    public BluetoothSpeakerAdapter(BluetoothSpeakerAPI api) {
        this.bluetoothApi = api;
    }

    @Override
    public void playAudio(Song song) {
        String payload = song.getTitle() + " by " + song.getArtist();
        bluetoothApi.playSoundViaBluetooth(payload);
    }
}
