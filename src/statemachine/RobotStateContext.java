package statemachine;

import robot.*;
import shared.ColorType;
import shared.UserCommandContainer;
import statemachine.common.OfflineState;
import util.ConsoleHelper;

import java.util.ArrayList;
import java.util.List;

public class RobotStateContext implements IRobotObserver, IStatemachineObservable {

    private RobotState currentState;
    private boolean stopCalled = false;

    private IRobot robot;
    public IRobot getRobot() {
        return robot;
    }

    protected List<IStatemachineObserver> observers = new ArrayList<>();

    // Calibration done flag
    private boolean calibrationDone = false;
    public boolean isCalibrationDone() {
        return this.calibrationDone;
    }
    public void setCalibrationDone(boolean calibrationDone) {
        this.calibrationDone = calibrationDone;
    }

    public RobotStateContext() {
        this.robot = RobotFactory.CreateRobot();
        this.currentState = new OfflineState();
    }

    public void setState(final RobotState newState) {
        // Backup mechanism to prevent unwanted transitions after stop has been called
        if (this.stopCalled && !(newState instanceof OfflineState)) return;

        StatemachineEventArgs eventArgsPreChange = new StatemachineEventArgs();
        eventArgsPreChange.stateName = newState.getName();
        eventArgsPreChange.stateNameWillChange = true;
        this.notifyObservers(eventArgsPreChange);

        this.currentState = newState;

        StatemachineEventArgs eventArgs = new StatemachineEventArgs();
        eventArgs.stateName = newState.getName();
        eventArgs.stateNameChanged = true;
        this.notifyObservers(eventArgs);

        this.currentState.writeMessage();
        this.currentState.initState(this);
    }

    @Override
    public void handleRobotMessage(RobotMessage message, String data) {
        this.currentState.handleRobotMessage(this, message, data);

        StatemachineEventArgs args = new StatemachineEventArgs();
        switch (message) {
            case AngleChanged: {
                args.angle = Float.parseFloat(data);
                args.angleChanged = true;
                break;
            }
            case ColorChanged: {
                args.colorName = data;
                args.colorNameChanged = true;
                break;
            }
            case DistanceChanged: {
                args.distance = Float.parseFloat(data);
                args.distanceChanged = true;
                break;
            }
            default: {
                args = null;
            }
        }

        if (args != null) {
            this.notifyObservers(args);
        }
    }

    public void startRobot() {
        this.stopCalled = false;
        ((IRobotObservable)this.robot).subscribeToRobotMessages(this);
        this.currentState.startRobot(this);
    }

    public void stopRobot() {
        this.stopCalled = true;
        this.currentState.stopRobot(this);
        ((IRobotObservable)this.robot).unsubscribeToRobotMessages(this);
    }

    public void forwardCommand(UserCommandContainer command) {
        this.currentState.handleCommand(this, command);
    }

    @Override
    public void subscribe(IStatemachineObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(IStatemachineObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(StatemachineEventArgs args) {
        for(IStatemachineObserver observer: this.observers) {
            observer.handleStatemachineEvent(args);
        }
    }
}
