package util;

import config.RobotConfig;
import util.interfaces.ICustomLogger;

public class CustomLogger implements ICustomLogger {

    @Override
    public String getDisplayName() {
        return this.getClass().getName();
    }

    @Override
    public boolean getLogToConsoleIsActive() {
        return RobotConfig.getWriteToConsole();
    }

    @Override
    public boolean getLogToFileIsActive() {
        return false;
    }

    @Override
    public void log(String message) {
        if(this.getLogToConsoleIsActive()) {
            ConsoleHelper.printlnDefault(this.createText(message));
        }
    }

    @Override
    public void log(String message, LoggingLevel loggingLevel) {
        if(this.getLogToConsoleIsActive()) {
            switch (loggingLevel) {
                case Debug: {
                    ConsoleHelper.printlnBlue(this.createText(message));
                    break;
                }
                case Message: {
                    ConsoleHelper.printlnDefault(this.createText(message));
                    break;
                }
                case Warning: {
                    ConsoleHelper.printlnYellow(this.createText(message));
                    break;
                }
                case Error: {
                    ConsoleHelper.printlnRed(this.createText(message));
                    break;
                }
            }
        }
    }

    private String createText(String message) {
        return String.format("%s >> %s", this.getDisplayName(), message);
    }
}
