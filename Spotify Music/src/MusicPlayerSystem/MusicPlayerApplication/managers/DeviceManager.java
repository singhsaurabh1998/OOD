package MusicPlayerSystem.MusicPlayerApplication.managers;

import MusicPlayerSystem.MusicPlayerApplication.device.IAudioOutputDevice;
import MusicPlayerSystem.MusicPlayerApplication.enums.DeviceType;
import MusicPlayerSystem.MusicPlayerApplication.factories.DeviceFactory;

/* Singleton class to manage audio output devices.
 * DeviceManager ensures that only one audio output device is connected at any time.
 * It provides methods to connect a device, retrieve the current device, and check if a device is connected.
 */
public class DeviceManager {
    private static DeviceManager instance = null;
    private IAudioOutputDevice currentOutputDevice;

    private DeviceManager() {
        currentOutputDevice = null;
    }

    public static synchronized DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }

    public void connect(DeviceType deviceType) {
        if (currentOutputDevice != null) {
            throw new RuntimeException("An output device is already connected. Please disconnect it first.");
        }

        currentOutputDevice = DeviceFactory.createDevice(deviceType);

        switch (deviceType) {
            case BLUETOOTH:
                System.out.println("Bluetooth device connected ");
                break;
            case WIRED:
                System.out.println("Wired device connected ");
                break;
            case HEADPHONES:
                System.out.println("Headphones connected ");
                break;
        }
    }

    public IAudioOutputDevice getOutputDevice() {
        if (currentOutputDevice == null) {
            throw new RuntimeException("No output device is connected.");
        }
        return currentOutputDevice;
    }

    public boolean hasOutputDevice() {
        return currentOutputDevice != null;
    }
}
