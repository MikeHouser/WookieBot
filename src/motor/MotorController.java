package motor;

import motor.statemachine.states.MotorControllerState;
import motor.statemachine.states.MotorControllerStateOffline;
import util.CustomLogger;
import util.LoggingLevel;

import java.util.ArrayList;
import java.util.List;

public class MotorController extends CustomLogger implements IMotorController, IMotorObserver, IMotorControllerObservable {

    private Axis axis = new Axis();

    public Axis getAxis() {
        return this.axis;
    }

    //region STATEMACHINE

    private MotorControllerState motorControllerState = new MotorControllerStateOffline();

    public void setMotorControllerState(MotorControllerState motorControllerState) {
        this.motorControllerState.deInitState(this);

        this.motorControllerState = motorControllerState;
        super.log(String.format("New state: %s", motorControllerState.getClass().getName()));

        this.motorControllerState.initState(this);
    }

    //endregion

    //region Observer Pattern
    private boolean finishUnsubscribeCalled = false;
    protected List<IMotorControllerObserver> observers = new ArrayList<>();
    protected List<IMotorControllerObserver> observersToRemove = new ArrayList<>();
    //endregion

    public MotorController(IMotor leftMotor, IMotor rightMotor) {
        this.axis.setLeftMotor(leftMotor);
        this.axis.setRightMotor(rightMotor);
    }

    @Override
    public boolean getIsMoving() {
        return this.axis.getIsMoving();
    }

    //region Driving Operations

    @Override
    public void startDriveForward() {
        super.log("startDriveForward");
        this.motorControllerState.startDriveForward(this);
    }

    @Override
    public void startDriveBackward() {
        super.log("startDriveBackward");
        this.motorControllerState.startDriveBackward(this);
    }

    @Override
    public void stop() {
        super.log("stop");
        this.motorControllerState.stop(this);
    }

    @Override
    public void startTurnLeft() {
        super.log("startTurnLeft");
        this.motorControllerState.startTurnLeft(this);
    }

    @Override
    public void startTurnRight() {
        super.log("startTurnRight");
        this.motorControllerState.startTurnRight(this);
    }

    //endregion

    @Override
    public void setSpeed(int speed) {
        super.log("MotorController: setSpeed");
        this.motorControllerState.setSpeed(this, speed);
    }

    @Override
    public void init() {
        super.log("init");

        this.motorControllerState.startMotorController(this);

        this.axis.init();

        ((IMotorObservable)this.axis.getLeftMotor()).subscribeToMotorMessages(this);
        ((IMotorObservable)this.axis.getRightMotor()).subscribeToMotorMessages(this);
    }

    @Override
    public void deInit() {
        super.log("deInit");

        this.motorControllerState.shutdownMotorController(this);

        this.axis.deInit();

        ((IMotorObservable)this.axis.getLeftMotor()).unsubscribeToMotorMessages(this);
        ((IMotorObservable)this.axis.getRightMotor()).unsubscribeToMotorMessages(this);

        ((IMotorObservable)this.axis.getLeftMotor()).finishUnsubscribeToMotorMessages();
        ((IMotorObservable)this.axis.getRightMotor()).finishUnsubscribeToMotorMessages();
    }

    //region Stepper Functionality

    @Override
    public void turnLeftWithSteps(int steps) {
        super.log(String.format("turnLeftWithSteps(%d)", steps));
        this.motorControllerState.turnLeftWithSteps(this, steps);
    }

    @Override
    public void turnRightWithSteps(int steps) {
        super.log(String.format("MotorController: turnRightWithSteps(%d)", steps));
        this.motorControllerState.turnRightWithSteps(this, steps);
    }

    //endregion

    //region Implement IMotorObserver

    @Override
    public void handleMotorMessage(MotorMessage message, MotorType motorType) {
        if(this.axis.getIsMoving()) {
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
            super.log(concurrentModificationException.toString(), LoggingLevel.Error);
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
