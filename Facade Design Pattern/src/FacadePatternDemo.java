class DVDPlayer {
    public void on() { System.out.println("DVD Player ON"); }
    public void play(String movie) { System.out.println("Playing movie: " + movie); }
    public void off() { System.out.println("DVD Player OFF"); }
}

class Projector {
    public void on() { System.out.println("Projector ON"); }
    public void off() { System.out.println("Projector OFF"); }
}

class SoundSystem {
    public void on() { System.out.println("Sound System ON"); }
    public void setVolume(int level) { System.out.println("Volume set to " + level); }
    public void off() { System.out.println("Sound System OFF"); }
}

class Lights {
    public void dim() { System.out.println("Lights dimmed"); }
    public void on() { System.out.println("Lights ON"); }
}
class HomeTheaterFacade {
    private DVDPlayer dvd;
    private Projector projector;
    private SoundSystem sound;
    private Lights lights;

    public HomeTheaterFacade(DVDPlayer dvd, Projector projector, SoundSystem sound, Lights lights) {
        this.dvd = dvd;
        this.projector = projector;
        this.sound = sound;
        this.lights = lights;
    }

    // Simplified method for user
    public void watchMovie(String movie) {
        System.out.println("Get ready to watch a movie...");
        lights.dim();
        projector.on();
        sound.on();
        sound.setVolume(5);
        dvd.on();
        dvd.play(movie);
    }

    public void endMovie() {
        System.out.println("Shutting movie theater down...");
        lights.on();
        projector.off();
        sound.off();
        dvd.off();
    }
}
public class FacadePatternDemo {
    public static void main(String[] args) {
        // Subsystems
        DVDPlayer dvd = new DVDPlayer();
        Projector projector = new Projector();
        SoundSystem sound = new SoundSystem();
        Lights lights = new Lights();

        // Facade
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(dvd, projector, sound, lights);

        // User interacts only with Facade
        homeTheater.watchMovie("Inception");
        homeTheater.endMovie();
    }
}
