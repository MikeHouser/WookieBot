package motor;

import util.ConsoleHelper;

public class StepperMotorBase extends MotorBase implements IStepperMotor, Runnable {

    protected SteppingMethod steppingMethod;
    protected Direction direction = Direction.FORWARD;
    protected boolean invertDirection = false;
    protected Thread thread = null;

    public StepperMotorBase(int minSpeedAbs, int maxSpeedAbs, MotorType motorType, boolean invertDirection, SteppingMethod steppingMethod) {
        super(minSpeedAbs, maxSpeedAbs, motorType);

        this.invertDirection = invertDirection;
        this.steppingMethod = steppingMethod;
    }

    private void startThread() {
        this.thread = new Thread(this);
        this.thread.start();
    }

    //region Implement IStepperMotor

    @Override
    public void step() throws InterruptedException {

    }

    @Override
    public void steps(int noOfSteps) throws InterruptedException {
        for (int currentStep = 0; currentStep < Math.abs(noOfSteps); currentStep++) {
            this.step();
        }
    }

    @Override
    public void angleRotation(float angle) throws InterruptedException{

    }

    @Override
    public void quarterRotation(int noOfQuarterRotations) throws InterruptedException{
        switch (steppingMethod) {
            case HALF_STEP:
                steps(2 * 512 * noOfQuarterRotations);
                break;
            default:
                steps(512 * noOfQuarterRotations);
                break;
        }
    }

    @Override
    public void halfRotation(int noOfHalfRotations) throws InterruptedException {
        quarterRotation(2*noOfHalfRotations);
    }

    @Override
    public void fullRotation(int noOfRotations) throws InterruptedException {
        halfRotation(2*noOfRotations);
    }

    @Override
    public void startRotation() {
        try {
            while (true) {
                if (Thread.interrupted()) {
                    ConsoleHelper.printlnDefault("StepperMotorBase: startRotation -> Interrupted");
                    return;
                } else {
                    this.step();
                }
            }
        } catch (InterruptedException e) {
            return;
        }
    }

    @Override
    public void stopRotation() {
        ConsoleHelper.printlnDefault("StepperMotorBase: stopRotation");

        this.thread.interrupt();
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
        if(this.invertDirection) {
            if(this.direction == Direction.FORWARD) {
                this.direction = Direction.BACKWARD;
            } else {
                this.direction = Direction.FORWARD;
            }
        }
    }

    @Override
    public void setSteppingMethod(SteppingMethod steppingMethod) {
        this.steppingMethod = steppingMethod;
    }

    //endregion

    //region Implement Runnable

    @Override
    public void run() {
        this.startRotation();
    }

    //endregion

    //region Override Motorbase

    @Override
    public void forward() {
        super.forward();

        this.setDirection(Direction.FORWARD);

        this.startThread();
    }

    @Override
    public void backward() {
        super.backward();

        this.setDirection(Direction.BACKWARD);

        this.startThread();
    }

    @Override
    public void stop() {
        this.stopRotation();
    }

    @Override
    public boolean isRotating() {
        super.isRotating = this.thread != null && this.thread.isAlive();
        return super.isRotating();
    }

    //endregion
}
