package robot;

public interface IRobotObservable {
    void subscribeToRobotMessages(IRobotObserver observer);
    void unsubscribeToRobotMessages(IRobotObserver observer);
    void notifyObsevers(RobotMessage message, String data);
    void finishUnsubscribeToRobotMessages();
}
