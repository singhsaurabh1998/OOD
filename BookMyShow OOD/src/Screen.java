import java.util.*;

public class Screen {
    private final String screenId;
    private final String screenName;
    private final List<Show> shows;

    public Screen(String screenId, String screenName) {
        this.screenId = screenId;
        this.screenName = screenName;
        this.shows = new ArrayList<>();
    }

    public void addShow(Show show) {
        shows.add(show);
    }

    // Getters
    public String getScreenId() { return screenId; }
    public String getScreenName() { return screenName; }
    public List<Show> getShows() { return shows; }

    @Override
    public String toString() {
        return "Screen{" +
                "screenId='" + screenId + '\'' +
                ", screenName='" + screenName + '\'' +
                ", shows=" + shows +
                '}';
    }
}
