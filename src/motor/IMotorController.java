package motor;

public interface IMotorController {
    void stop();
    void startTurnLeft();
    void startTurnRight();
    void startDriveForward();
    void startDriveBackward();
    void setSpeed(int speed);
    void init();
    void deInit();
}