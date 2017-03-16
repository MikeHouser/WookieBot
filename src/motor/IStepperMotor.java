package motor;

public interface IStepperMotor {

    void step() throws InterruptedException;
    void steps(int noOfSteps) throws InterruptedException;
    void angleRotation(float angle) throws InterruptedException;
    void quarterRotation(int noOfQuarterRotations) throws InterruptedException;
    void halfRotation(int noOfHalfRotations) throws InterruptedException;
    void fullRotation(int noOfRotations) throws InterruptedException;

    void startRotationAsync(int noOfSteps);
    void stopRotation();

    void forward(int noOfSteps);
    void backward(int noOfSteps);

    void setDirection(Direction direction);
    void setSteppingMethod(SteppingMethod steppingMethod);
}
