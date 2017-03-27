package motor;

public interface IMotorControllerObservable {
    void subscribeToMotorControllerMessages(IMotorControllerObserver observer);
    void unsubscribeToMotorControllerMessages(IMotorControllerObserver observer);
    void notifyMotorControllerObservers(MotorControllerMessage message);
    void finishUnsubscribeToMotorControllerMessages();
}
