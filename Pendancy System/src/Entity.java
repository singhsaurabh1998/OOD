import java.util.List;

public class Entity {
    private final int id;
    private final List<String> tags;//tages rahenge list me children ki form me

    public Entity(int id, List<String> tags) {
        this.id = id;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }
}
