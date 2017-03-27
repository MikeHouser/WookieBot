package motor;

public interface IMotor {
    void setSpeed(int speedInPercent);
    int getSpeed();
    void forward();
    void backward();
    void stop();
    void init();
    void deInit();
    boolean isRotating();
    void setIsRotating(boolean isRotating);
}
