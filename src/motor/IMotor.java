package motor;

public interface IMotor {
    void setSpeed(int speedInPercent);
    void forward();
    void backward();
    void stop();
    void init();
    void deInit();
    boolean isRotating();
}
