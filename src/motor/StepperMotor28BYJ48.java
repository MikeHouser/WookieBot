package motor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import config.RobotConfig;
import util.ConsoleHelper;

import static com.pi4j.io.gpio.PinState.LOW;
import static com.pi4j.io.gpio.PinState.HIGH;

/**
 * Stepper Motor 28BYJ-48 with ULN2003 Board
 * Introduction video: https://www.youtube.com/watch?v=B86nqDRskVU
 */
public class StepperMotor28BYJ48 extends StepperMotorBase {

    //region Private Fields

    private int lastSequenceIndex = 0;
    private GpioPinDigitalOutput[] motorPins;

    //endregion

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
        super(RobotConfig.getStepperMinSpeedAbs(), RobotConfig.getStepperMaxSpeedAbs(), motorType, invertDirection, steppingMethod);

        GpioController gpio = GpioFactory.getInstance();

        motorPins = new GpioPinDigitalOutput[4];
        motorPins[0] = gpio.provisionDigitalOutputPin(pinA, "Pin A", LOW);
        motorPins[1] = gpio.provisionDigitalOutputPin(pinB, "Pin B", LOW);
        motorPins[2] = gpio.provisionDigitalOutputPin(pinC, "Pin C", LOW);
        motorPins[3] = gpio.provisionDigitalOutputPin(pinD, "Pin D", LOW);
    }

    @Override
    public void angleRotation(float angle) throws InterruptedException {
        int steps;
        switch (super.steppingMethod) {
            case HALF_STEP:
                steps = (int) (512 * 8 * angle) / 360;
                break;
            default:
                steps = (int) (512 * 4 * angle) / 360;
                break;
        }
        steps(steps);
    }

    public void step() throws InterruptedException  {
        if (super.direction == Direction.FORWARD) {
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

    private void writeSequence(int sequenceNo) throws InterruptedException {
        for (int i = 0; i < 4; i++) {
            switch(super.steppingMethod) {
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
            ConsoleHelper.printlnDefault("StepperMotor28BYJ48: writeSequence -> Interrupted");
            throw e;
        }
    }
}