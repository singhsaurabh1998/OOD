import java.util.List;

public class Movie {
    private final String movieId;
    private final String title;
    private final String language;
    private final int durationInMinutes;
    private final List<String> genres;

    public Movie(String movieId, String title, String language, int durationInMinutes, List<String> genres) {
        this.movieId = movieId;
        this.title = title;
        this.language = language;
        this.durationInMinutes = durationInMinutes;
        this.genres = genres;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public List<String> getGenres() {
        return genres;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId='" + movieId + '\'' +
                ", title='" + title + '\'' +
                ", language='" + language + '\'' +
                ", durationInMinutes=" + durationInMinutes +
                ", genres=" + genres +
                '}';
    }
}
