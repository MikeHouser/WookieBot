package motor;

public interface IMotorObservable {
    void subscribeToMotorMessages(IMotorObserver observer);
    void unsubscribeToMotorMessages(IMotorObserver observer);
    void notifyMotorObservers(MotorMessage message, MotorType motorType);
    void finishUnsubscribeToMotorMessages();
}