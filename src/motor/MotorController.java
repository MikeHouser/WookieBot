package motor;

import util.ConsoleHelper;

public class MotorController implements IMotorController {
    private IMotor leftMotor;
    private IMotor rightMotor;
    private boolean waitForStop = false;

    public MotorController(IMotor leftMotor, IMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    private void waitForMotorsToStop() {
        while(this.leftMotor.isRotating() || this.rightMotor.isRotating()) {

            try {
                if(!this.waitForStop) {
                    // Print this message just one time
                    ConsoleHelper.printlnDefault("One motor is still rotating.");
                }
                this.waitForStop = true;

                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.waitForStop = false;
    }

    @Override
    public void stop() {
        ConsoleHelper.printlnDefault("MotorController: stop");
        this.leftMotor.stop();
        this.rightMotor.stop();
    }

    @Override
    public void startTurnLeft() {
        ConsoleHelper.printlnDefault("MotorController: startTurnLeft");
        this.waitForMotorsToStop();
        this.leftMotor.backward();
        this.rightMotor.forward();
    }

    @Override
    public void startTurnRight() {
        ConsoleHelper.printlnDefault("MotorController: startTurnRight");
        this.waitForMotorsToStop();
        this.leftMotor.forward();
        this.rightMotor.backward();
    }

    @Override
    public void startDriveForward() {
        ConsoleHelper.printlnDefault("MotorController: startDriveForward");
        this.waitForMotorsToStop();
        this.leftMotor.forward();
        this.rightMotor.forward();
    }

    @Override
    public void startDriveBackward() {
        ConsoleHelper.printlnDefault("MotorController: startDriveBackward");
        this.waitForMotorsToStop();
        this.leftMotor.backward();
        this.rightMotor.backward();
    }

    @Override
    public void setSpeed(int speed) {
        ConsoleHelper.printlnDefault("MotorController: setSpeed");
        this.leftMotor.setSpeed(speed);
        this.rightMotor.setSpeed(speed);
    }

    @Override
    public void init() {
        ConsoleHelper.printlnDefault("MotorController: init");
    }

    @Override
    public void deInit() {
        ConsoleHelper.printlnDefault("MotorController: deInit");
        this.leftMotor.deInit();
        this.rightMotor.deInit();
    }
}
