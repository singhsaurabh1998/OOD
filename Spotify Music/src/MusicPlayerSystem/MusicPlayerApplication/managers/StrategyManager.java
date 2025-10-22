package MusicPlayerSystem.MusicPlayerApplication.managers;

import MusicPlayerSystem.MusicPlayerApplication.enums.PlayStrategyType;
import MusicPlayerSystem.MusicPlayerApplication.strategies.*;

public class StrategyManager {
    private static StrategyManager instance = null;
    private final SequentialPlayStrategy sequentialStrategy;
    private final RandomPlayStrategy randomStrategy;
    private final CustomQueueStrategy customQueueStrategy;

    private StrategyManager() {
        sequentialStrategy = new SequentialPlayStrategy();
        randomStrategy = new RandomPlayStrategy();
        customQueueStrategy = new CustomQueueStrategy();
    }

    public static synchronized StrategyManager getInstance() {
        if (instance == null) {
            instance = new StrategyManager();
        }
        return instance;
    }

    public PlayStrategy getStrategy(PlayStrategyType type) {
        if (type == PlayStrategyType.SEQUENTIAL) {
            return sequentialStrategy;
        } else if (type == PlayStrategyType.RANDOM) {
            return randomStrategy;
        } else {
            return customQueueStrategy;
        }
    }
}
