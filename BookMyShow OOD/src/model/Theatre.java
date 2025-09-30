package model;

import java.util.*;

public class Theatre {
    private final String theatreId;
    private final String theaterName;
    private final String city;
    private final List<Screen> screens;

    public Theatre(String theatreId, String name, String city) {
        this.theatreId = theatreId;
        this.theaterName = name;
        this.city = city;
        this.screens = new ArrayList<>();
    }

    public void addScreen(Screen screen) {
        screens.add(screen);
    }

    // Getters
    public String getTheatreId() { return theatreId; }
    public String getName() { return theaterName; }
    public String getCity() { return city; }
    public List<Screen> getScreens() { return screens; }

    @Override
    public String toString() {
        return "Theatre{" +
                "theatreId='" + theatreId + '\'' +
                ", theaterName='" + theaterName + '\'' +
                ", city='" + city + '\'' +
                ", screens=" + screens +
                '}';
    }
}
