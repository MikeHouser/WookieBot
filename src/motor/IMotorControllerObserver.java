package motor;

public interface IMotorControllerObserver {
    void handleMotorControllerMessage(MotorControllerMessage message);
}
