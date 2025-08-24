
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class Jumper {
    int startPoint;
    int endPoint;
    JumperType type;

    public Jumper(int startPoint, int endPoint,JumperType type) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.type = type;
    }
}