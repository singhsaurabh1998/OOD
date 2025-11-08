package MusicPlayerSystem.MusicPlayerApplication.factories;

import MusicPlayerSystem.MusicPlayerApplication.device.*;
import MusicPlayerSystem.MusicPlayerApplication.enums.DeviceType;
import MusicPlayerSystem.MusicPlayerApplication.external.*;

public class DeviceFactory {
    public static IAudioOutputDevice createDevice(DeviceType deviceType) {
        switch (deviceType) {
            case BLUETOOTH:
                return new BluetoothSpeakerAdapter(new BluetoothSpeakerAPI());
            case WIRED:
                return new WiredSpeakerAdapter(new WiredSpeakerAPI());
            case HEADPHONES:
            default:
                return new HeadphonesAdapter(new HeadphonesAPI());
        }
    }
}
