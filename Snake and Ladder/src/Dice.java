import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class Dice {
    private int numberOfDice;

    Dice(int numberOfDice) {
        this.numberOfDice = numberOfDice;
    }

    int rollDice() {
        int sum = 0;
        for (int i = 0; i < numberOfDice; i++) {
            sum += (int) (Math.random() * 6) + 1;
        }
        return sum;
    }
}
