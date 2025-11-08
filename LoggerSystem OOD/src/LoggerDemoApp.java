import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Enum representing the supported log levels.
 * The order is important: DEBUG < INFO < WARNING < ERROR
 */
enum LogType {
    DEBUG, INFO, WARNING, ERROR;
}

enum LogWriterMethod {
    CONSOLE,
    FILE
    // Add DATABASE etc. in future
}


/**
 * Strategy pattern:
 * LogWriter defines interface for writing logs.
 * Allows switching between different writers (console, file, db) easily.
 */
interface LogWriter {
    /**
     * Writes a log message.
     *
     * @param formattedMessage The formatted message with timestamp & level.
     */
    void write(String formattedMessage);
}

/**
 * Concrete Strategy:
 * Writes log to the console (System.out).
 */
class ConsoleLogWriter implements LogWriter {
    @Override
    public void write(String formattedMessage) {
        System.out.println("ConsoleLogWriter : " + formattedMessage);
    }
}

/**
 * Concrete Strategy:
 * Simulates writing log to a file.
 * For demo, just prints with "(File)" prefix.
 */
class FileLogWriter implements LogWriter {
    @Override
    public void write(String formattedMessage) {
        // In real code, you'd open a file & append
        System.out.println("(File) " + formattedMessage);
    }
}

/**
 * Factory Pattern:
 * Creates LogWriter objects based on type.
 */
class LogWriterFactory {
    /**
     * Get a LogWriter based on given type.
     *
     * @param type CONSOLE, FILE, etc.
     * @return LogWriter instance
     */
    public static LogWriter getLogWriter(LogWriterMethod type) {
        switch (type) {
            case CONSOLE:
                return new ConsoleLogWriter();
            case FILE:
                return new FileLogWriter();
            // case DATABASE: return new DatabaseLogWriter();
            default:
                throw new IllegalArgumentException("Unknown LogWriterMethod: " + type);
        }
    }
}

/**
 * Singleton: Service Class
 * Logger ensures only one instance exists.
 * Also uses Strategy to decide which LogWriter to use.
 */
class Logger {
    private static final Logger instance = new Logger();

    private LogType currentLevel;
    private LogWriter logWriter;

    // private constructor so others cannot create instance
    private Logger() {
    }

    /**
     * Get the single Logger instance.
     */
    public static Logger getInstance() {
        return instance;
    }

    /**
     * Set the minimum log level to be displayed.
     *
     * @param level e.g., INFO, WARNING, etc.
     */
    public void setLogType(LogType level) {
        this.currentLevel = level;
    }

    /**
     * Set the LogWriter strategy.
     *
     * @param writer ConsoleLogWriter, FileLogWriter, etc.
     */
    public void setLogWriter(LogWriter writer) {
        this.logWriter = writer;
    }

    /**
     * Logs a message with same level(mtlb info  h to info wale bbss)
     * Adds timestamp & level.
     *
     * @param message The message to log
     * @param level   The level of this message
     */
    public void log(String message, LogType level) {
        // Filter out messages below currentLevel
        if (level == currentLevel) {
            // Format: timestamp + level + message
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String formatted = "[" + timestamp + "] [" + level + "] " + message;

            // Delegate actual writing to the LogWriter
            logWriter.write(formatted);
        }
    }
}

public class LoggerDemoApp {
    public static void main(String[] args) {
        // Get singleton logger
        Logger logger = Logger.getInstance();

        LogWriter console = LogWriterFactory.getLogWriter(LogWriterMethod.FILE);
        logger.setLogWriter(console);
        logger.setLogType(LogType.INFO);
        // By default, level = INFO, writer = console

        logger.log("This is a debug msg", LogType.DEBUG);   // won't show
        logger.log("This is an info msg", LogType.INFO);    // will show
        logger.log("This is a warning msg", LogType.WARNING); // will show
        logger.log("This is an error msg", LogType.ERROR);    // will show
        logger.log("This is an info msg", LogType.INFO);    // will show

        // Change to DEBUG to see all logs
        logger.setLogType(LogType.DEBUG);
        logger.log("Now DEBUG msg appears", LogType.DEBUG);

        // Switch to FileLogWriter
        logger.setLogWriter(new FileLogWriter());
        logger.log("This log goes to simulated file", LogType.INFO);
    }
}
