package motor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import config.RobotConfig;

import static com.pi4j.io.gpio.PinState.LOW;
import static com.pi4j.io.gpio.PinState.HIGH;

/**
 * Stepper Motor 28BYJ-48 with ULN2003 Board
 * Introduction video: https://www.youtube.com/watch?v=B86nqDRskVU
 */
public class StepperMotor28BYJ48 extends MotorBase implements Runnable {

    private int lastSequenceIndex = 0;
    private Direction direction = Direction.FORWARD;
    private Thread thread = null;
    private GpioPinDigitalOutput[] motorPins;
    private SteppingMethod steppingMethod;
    private boolean invertDirection = false;

    public enum SteppingMethod {
        WAVE_DRIVE, FULL_STEP, HALF_STEP
    }

    public enum Direction {
        FORWARD, BACKWARD
    }

    //region Sequences

    private static final PinState WAVE_DRIVE_MOTOR_SEQUENCE[][] =
            new PinState[][] {
                    { HIGH, LOW,  LOW,  LOW },
                    { LOW,  HIGH, LOW,  LOW },
                    { LOW,  LOW,  HIGH, LOW },
                    { LOW,  LOW,  LOW,  HIGH },
                    { HIGH, LOW,  LOW,  LOW },
                    { LOW,  HIGH, LOW,  LOW },
                    { LOW,  LOW,  HIGH, LOW },
                    { LOW,  LOW,  LOW,  HIGH }
            };

    private static final PinState FULL_STEP_MOTOR_SEQUENCE[][] =
            new PinState[][] {
                    { HIGH, HIGH, LOW,  LOW  },
                    { LOW,  HIGH, HIGH, LOW  },
                    { LOW,  LOW,  HIGH, HIGH },
                    { HIGH, LOW,  LOW,  HIGH },
                    { HIGH, HIGH, LOW,  LOW  },
                    { LOW,  HIGH, HIGH, LOW  },
                    { LOW,  LOW,  HIGH, HIGH },
                    { HIGH, LOW,  LOW,  HIGH }
            };

    private static final PinState HALF_STEP_MOTOR_SEQUENCE[][] =
            new PinState[][] {
                    { HIGH, LOW,  LOW,  LOW },
                    { HIGH, HIGH, LOW,  LOW },
                    { LOW,  HIGH, LOW,  LOW },
                    { LOW,  HIGH, HIGH, LOW },
                    { LOW,  LOW,  HIGH, LOW },
                    { LOW,  LOW,  HIGH, HIGH },
                    { LOW,  LOW,  LOW,  HIGH },
                    { HIGH, LOW,  LOW,  HIGH }
            };

    //endregion

    public StepperMotor28BYJ48(Pin pinA, Pin pinB, Pin pinC, Pin pinD, SteppingMethod steppingMethod, MotorType motorType, boolean invertDirection)
    {
        super(RobotConfig.getStepperMinSpeedAbs(), RobotConfig.getStepperMaxSpeedAbs(), motorType);

        this.invertDirection = invertDirection;

        GpioController gpio = GpioFactory.getInstance();

        motorPins = new GpioPinDigitalOutput[4];
        motorPins[0] = gpio.provisionDigitalOutputPin(pinA, "Pin A", LOW);
        motorPins[1] = gpio.provisionDigitalOutputPin(pinB, "Pin B", LOW);
        motorPins[2] = gpio.provisionDigitalOutputPin(pinC, "Pin C", LOW);
        motorPins[3] = gpio.provisionDigitalOutputPin(pinD, "Pin D", LOW);

        this.steppingMethod = steppingMethod;
    }

    public void fullRotation(int noOfRotations) throws InterruptedException {
        halfRotation(2*noOfRotations);
    }

    public void halfRotation(int noOfHalfRotations) throws InterruptedException {
        quarterRotation(2*noOfHalfRotations);
    }

    public void quarterRotation(int noOfQuarterRotations) throws InterruptedException {
        switch (steppingMethod) {
            case HALF_STEP:
                step(2 * 512 * noOfQuarterRotations);
                break;
            default:
                step(512 * noOfQuarterRotations);
                break;
        }
    }

    public void angleRotation(float angle) throws InterruptedException {
        int steps;
        switch (steppingMethod) {
            case HALF_STEP:
                steps = (int) (512 * 8 * angle) / 360;
                break;
            default:
                steps = (int) (512 * 4 * angle) / 360;
                break;
        }
        step(steps);
    }

    public void step(int noOfSteps) throws InterruptedException {
        for (int currentStep = 0; currentStep < Math.abs(noOfSteps); currentStep++) {
            this.oneStep();
        }
    }

    public void oneStep() throws InterruptedException {
        if (this.direction == Direction.FORWARD) {
            this.lastSequenceIndex++;
            if (this.lastSequenceIndex > 7) {
                this.lastSequenceIndex = 0;
            }
        } else {
            this.lastSequenceIndex--;
            if (this.lastSequenceIndex < 0) {
                this.lastSequenceIndex = 7;
            }
        }
        writeSequence(this.lastSequenceIndex);
    }

    public void startRotation() throws InterruptedException {
        while(true) {
            if(Thread.interrupted()) {
                throw new InterruptedException();
            } else {
                this.oneStep();
            }
        }
    }

    private void writeSequence(int sequenceNo) throws InterruptedException {
        for (int i = 0; i < 4; i++) {
            switch(steppingMethod) {
                case WAVE_DRIVE:
                    motorPins[i].setState(WAVE_DRIVE_MOTOR_SEQUENCE[sequenceNo][i]);
                    break;
                case FULL_STEP:
                    motorPins[i].setState(FULL_STEP_MOTOR_SEQUENCE[sequenceNo][i]);
                    break;
                default:
                    motorPins[i].setState(HALF_STEP_MOTOR_SEQUENCE[sequenceNo][i]);
                    break;
            }
        }

        try {
            Thread.sleep(super.currentSpeed);
        } catch (InterruptedException e) {
            throw e;
        }
    }

    @Override
    public void run() {
        try {
            this.startRotation();
        } catch (InterruptedException e) {

        }
    }

    private void startThread() {
        this.thread = new Thread(this);
        this.thread.start();
    }

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
        super.stop();

        this.thread.interrupt();
    }

    @Override
    public boolean isRotating() {
        super.isRotating = this.thread != null && this.thread.isAlive();
        return super.isRotating();
    }

    private void setDirection(Direction direction) {
        this.direction = direction;
        if(this.invertDirection) {
            if(this.direction == Direction.FORWARD) {
                this.direction = Direction.BACKWARD;
            } else {
                this.direction = Direction.FORWARD;
            }
        }
    }
}