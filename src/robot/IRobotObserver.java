package robot;

/**
 * Created by mike on 09.05.16.
 */

public interface IRobotObserver {
    void handleRobotMessage(RobotMessage message, String data);
}
