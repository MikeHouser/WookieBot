package robot;

/**
 * Created by mike on 09.05.16.
 */
public interface IRobotObservable {
    void subscribeToRobotMessages(IRobotObserver observer);
    void unsubscribeToRobotMessages(IRobotObserver observer);
    void notifyObsevers(RobotMessage message, String data);
    void finishUnsubscribeToRobotMessages();
}
