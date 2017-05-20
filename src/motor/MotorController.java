package motor;

import util.ConsoleHelper;

import java.util.ArrayList;
import java.util.List;

public class MotorController implements IMotorController, IMotorObserver, IMotorControllerObservable {
    private IMotor leftMotor;
    private IMotor rightMotor;
    private boolean waitForStop = false;
    private final boolean CONSOLE_OUTPUT = false;

    //region Observer Pattern
    private boolean finishUnsubscribeCalled = false;
    protected List<IMotorControllerObserver> observers = new ArrayList<>();
    protected List<IMotorControllerObserver> observersToRemove = new ArrayList<>();
    //endregion

    public MotorController(IMotor leftMotor, IMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    @Override
    public boolean getIsMoving() {
        return this.leftMotor.isRotating() || this.rightMotor.isRotating();
    }

    @Override
    public void stop() {
        if (CONSOLE_OUTPUT)
            ConsoleHelper.printlnDefault("MotorController: stop");
        this.leftMotor.stop();
        this.rightMotor.stop();
    }

    @Override
    public void startTurnLeft() {
        if (CONSOLE_OUTPUT)
            ConsoleHelper.printlnDefault("MotorController: startTurnLeft");
        this.leftMotor.backward();
        this.rightMotor.forward();
    }

    @Override
    public void startTurnRight() {
        if (CONSOLE_OUTPUT)
            ConsoleHelper.printlnDefault("MotorController: startTurnRight");
        this.leftMotor.forward();
        this.rightMotor.backward();
    }

    @Override
    public void startDriveForward() {
        if (CONSOLE_OUTPUT)
            ConsoleHelper.printlnDefault("MotorController: startDriveForward");
        this.leftMotor.forward();
        this.rightMotor.forward();
    }

    @Override
    public void startDriveBackward() {
        if (CONSOLE_OUTPUT)
            ConsoleHelper.printlnDefault("MotorController: startDriveBackward");
        this.leftMotor.backward();
        this.rightMotor.backward();
    }

    @Override
    public void setSpeed(int speed) {
        if (CONSOLE_OUTPUT)
            ConsoleHelper.printlnDefault("MotorController: setSpeed");
        this.leftMotor.setSpeed(speed);
        this.rightMotor.setSpeed(speed);
    }

    @Override
    public void init() {
        if (CONSOLE_OUTPUT)
            ConsoleHelper.printlnDefault("MotorController: init");

        ((IMotorObservable)this.leftMotor).subscribeToMotorMessages(this);
        ((IMotorObservable)this.rightMotor).subscribeToMotorMessages(this);
    }

    @Override
    public void deInit() {
        if (CONSOLE_OUTPUT)
            ConsoleHelper.printlnDefault("MotorController: deInit");

        this.leftMotor.deInit();
        this.rightMotor.deInit();

        ((IMotorObservable)this.leftMotor).unsubscribeToMotorMessages(this);
        ((IMotorObservable)this.rightMotor).unsubscribeToMotorMessages(this);

        ((IMotorObservable)this.leftMotor).finishUnsubscribeToMotorMessages();
        ((IMotorObservable)this.rightMotor).finishUnsubscribeToMotorMessages();
    }

    //region Stepper Functionality

    @Override
    public void turnLeftWithSteps(int steps) {
        if (CONSOLE_OUTPUT)
            ConsoleHelper.printlnDefault(String.format("MotorController: turnLeftWithSteps(%d)", steps));

        if(this.leftMotor instanceof IStepperMotor && this.rightMotor instanceof IStepperMotor) {
            ((IStepperMotor) this.leftMotor).backward(steps);
            ((IStepperMotor) this.rightMotor).forward(steps);
        } else {
            if (CONSOLE_OUTPUT)
                ConsoleHelper.printlnYellow("Warning: At least one motor is not an instance of IStepperMotor.");
        }
    }

    @Override
    public void turnRightWithSteps(int steps) {
        if (CONSOLE_OUTPUT)
            ConsoleHelper.printlnDefault(String.format("MotorController: turnRightWithSteps(%d)", steps));

        if(this.leftMotor instanceof IStepperMotor && this.rightMotor instanceof IStepperMotor) {
            ((IStepperMotor) this.leftMotor).forward(steps);
            ((IStepperMotor) this.rightMotor).backward(steps);
        } else {
            if (CONSOLE_OUTPUT)
                ConsoleHelper.printlnYellow("Warning: At least one motor is not an instance of IStepperMotor.");
        }
    }

    //endregion

    //region Implement IMotorObserver

    @Override
    public void handleMotorMessage(MotorMessage message, MotorType motorType) {
        if(this.leftMotor.isRotating() || this.rightMotor.isRotating()) {
            // at least one motor is rotating
            this.notifyMotorControllerObservers(MotorControllerMessage.MovementStarted);
        } else {
            // no motor is rotating
            this.notifyMotorControllerObservers(MotorControllerMessage.MovementStopped);
        }
    }

    //endregion

    //region Implement IMotorControllerObservable

    @Override
    public void subscribeToMotorControllerMessages(IMotorControllerObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void unsubscribeToMotorControllerMessages(IMotorControllerObserver observer) {
        this.observersToRemove.add(observer);
    }

    @Override
    public void notifyMotorControllerObservers(MotorControllerMessage message) {
        try {
            for (IMotorControllerObserver observer : this.observers) observer.handleMotorControllerMessage(message);
        } catch (java.util.ConcurrentModificationException concurrentModificationException) {
            System.out.println(concurrentModificationException);
        }

        if(this.finishUnsubscribeCalled) {
            this.observers.removeAll(this.observersToRemove);
            this.observersToRemove.clear();

            this.finishUnsubscribeCalled = false;
        }
    }

    @Override
    public void finishUnsubscribeToMotorControllerMessages() {
        this.finishUnsubscribeCalled = true;
    }

    //endregion
}
