package motor;

public class Axis {
    private IMotor leftMotor;
    public IMotor rightMotor;

    public IMotor getLeftMotor() {
        return leftMotor;
    }

    public void setLeftMotor(IMotor leftMotor) {
        this.leftMotor = leftMotor;
    }

    public IMotor getRightMotor() {
        return rightMotor;
    }

    public void setRightMotor(IMotor rightMotor) {
        this.rightMotor = rightMotor;
    }

    public boolean getIsMoving() {
        return this.leftMotor.isRotating() || this.rightMotor.isRotating();
    }

    public void startForward() {
        this.leftMotor.forward();
        this.rightMotor.forward();
    }

    public void startDriveBackward() {
        this.leftMotor.backward();
        this.rightMotor.backward();
    }

    public void stop() {
        this.leftMotor.stop();
        this.rightMotor.stop();
    }

    public void startTurnLeft() {
        this.leftMotor.backward();
        this.rightMotor.forward();
    }

    public void startTurnRight() {
        this.leftMotor.forward();
        this.rightMotor.backward();
    }

    public void setSpeed(int speed) {
        this.leftMotor.setSpeed(speed);
        this.rightMotor.setSpeed(speed);
    }

    public void init() {
        this.leftMotor.init();
        this.rightMotor.init();
    }

    public void deInit() {
        this.leftMotor.deInit();
        this.rightMotor.deInit();
    }

    public void turnLeftWithSteps(int steps) {
        if(this.getHasStepperMotors()) {
            ((IStepperMotor) this.leftMotor).backward(steps);
            ((IStepperMotor) this.rightMotor).forward(steps);
        }
    }

    public void turnRightWithSteps(int steps) {
         if(this.getHasStepperMotors()) {
            ((IStepperMotor) this.leftMotor).forward(steps);
            ((IStepperMotor) this.rightMotor).backward(steps);
        }
    }

    private boolean getHasStepperMotors() {
        return this.leftMotor instanceof IStepperMotor && this.rightMotor instanceof IStepperMotor;
    }
}
