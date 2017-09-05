package util.interfaces;

import util.LoggingLevel;

public interface ICustomLogger {
    String getDisplayName();
    boolean getLogToConsoleIsActive();
    boolean getLogToFileIsActive();
    void log(String message);
    void log(String message, LoggingLevel loggingLevel);
}
