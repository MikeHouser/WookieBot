package robot;

import colorsensor.*;
import compass.CompassController;
import compass.IAngleObservable;
import compass.IAngleObserver;
import config.RobotConfig;
import distancesensor.DistanceController;
import distancesensor.IDistanceObserver;
import led.ILed;
import motor.MotorController;
import shared.ColorType;
import shared.ISensorController;

import java.util.List;
import java.util.ArrayList;

public class RobotBase implements IRobot, IAngleObserver, IRobotObservable, IColorObserver, IDistanceObserver {

	protected CompassController compassController;
    protected List<IRobotObserver> robotObservers = new ArrayList<IRobotObserver>();
    protected List<IRobotObserver> robotObserversToRemove = new ArrayList<IRobotObserver>();
    private boolean finishUnsubscribeToRobotMessagesCalled = false;
    protected ColorController colorController;
    protected ILed led;
    protected MotorController motorController;
    protected DistanceController distanceController;
	
	public RobotBase() {

    }

    public void initRobot() {
	    this.motorController.init();
	    this.motorController.setSpeed(RobotConfig.DefaultMotorSpeed);

	    this.distanceController.init();

	    this.led.init();
	    this.led.activate();

        this.colorController.init();

        // Subscribe
        this.compassController.subscribeToAngleChange(this);
        this.colorController.subscribeToColorChange(this);
        this.distanceController.subscribeToDistanceChange(this);
    }

    public void initRobotComplete() {
        this.notifyObsevers(RobotMessage.Initialized);
    }

    public void deinitRobot() {

        // Unsubscribe
        this.compassController.unsubscribeToAngleChange(this);
        this.compassController.finishUnsubscribeToAngleChange();

        this.colorController.unsubscribeToColorChange(this);
        this.colorController.finishUnsubscribeToColorChange();

        this.distanceController.unsubscribeToDistanceChange(this);
        this.distanceController.finishUnsubscribeToDistanceChange();

        // Stop listening
        this.compassController.stopListening();
        this.colorController.stopListening();
        this.distanceController.stopListening();

        // DeInit
        this.compassController.deInit();
        this.colorController.deInit();
        this.distanceController.deInit();
        this.motorController.deInit();
        this.led.deInit();

        this.finishUnsubscribeToRobotMessages();
    }
	
	public void calibrateCompass() {
        this.notifyObsevers(RobotMessage.CalibrationStarted);

		this.compassController.startCalibrating();
		this.performCalibrationDrive();
		this.compassController.stopCalibrating();

        this.notifyObsevers(RobotMessage.CalibrationFinished);
	}

    public void angleChanged(float angle) {
	    this.notifyObsevers(RobotMessage.AngleChanged, Float.toString(angle));
    }
	
	public void performCalibrationDrive() {
        this.Beep();

        this.motorController.startTurnLeft();

        try {
            Thread.sleep(RobotConfig.CalibrationDriveDuration_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.motorController.stop();

        try {
            Thread.sleep(RobotConfig.CalibrationDrivePause_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.motorController.startTurnRight();

        try {
            Thread.sleep(RobotConfig.CalibrationDriveDuration_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.motorController.stop();

        this.Beep();
    }

    public void startTurnLeft() {
	    this.motorController.startTurnLeft();
        this.notifyObsevers(RobotMessage.TurnLeftStarted);
    }

    public void stopTurnLeft() {
	    this.motorController.stop();
        this.notifyObsevers(RobotMessage.TurnLeftStopped);
    }

    public void startTurnRight() {
	    this.motorController.startTurnRight();
        this.notifyObsevers(RobotMessage.TurnRightStarted);
    }

    public void stopTurnRight() {
	    this.motorController.stop();
        this.notifyObsevers(RobotMessage.TurnRightStopped);
    }

    public void startDriveForward() {
	    this.motorController.startDriveForward();
        this.notifyObsevers(RobotMessage.DriveForwardStarted);
    }

    public void stopDriveForward() {
	    this.motorController.stop();
        this.notifyObsevers(RobotMessage.DriveForwardStopped);
    }

    public void startDriveBackward() {
	    this.motorController.startDriveBackward();
	    this.notifyObsevers(RobotMessage.DriveBackwardStarted);
	}

    public void stopDriveBackward() {
	    this.motorController.stop();
	    this.notifyObsevers(RobotMessage.DriveForwardStopped);
	}

    public void printMessage(String prefix, String message) {
        System.out.println(prefix + message);
    }

    public void printMessage(String message) {
        this.printMessage("RobotBase: ", message);
    }

    @Override
    public void subscribeToRobotMessages(IRobotObserver observer) {
        this.robotObservers.add(observer);
    }

    @Override
    public void unsubscribeToRobotMessages(IRobotObserver observer) {
        this.robotObserversToRemove.add(observer);
    }

    private void notifyObsevers(RobotMessage message) {
	    this.notifyObsevers(message, "");
    }

    @Override
    public void notifyObsevers(RobotMessage message, String data) {
        try {
            for (IRobotObserver observer : this.robotObservers) observer.handleRobotMessage(message, data);
        } catch (java.util.ConcurrentModificationException concurrentModificationException) {
            System.out.println(concurrentModificationException);
        }

        if(this.finishUnsubscribeToRobotMessagesCalled) {
            this.robotObservers.removeAll(this.robotObserversToRemove);
            this.robotObserversToRemove.clear();

            this.finishUnsubscribeToRobotMessagesCalled = false;
        }
    }

    @Override
    public void finishUnsubscribeToRobotMessages() {
        this.finishUnsubscribeToRobotMessagesCalled = true;
    }

    @Override
    public void colorChanged(ColorType colorType) {
        this.notifyObsevers(RobotMessage.ColorChanged, colorType.name());
    }

    @Override
    public float getAngle() {
        return this.compassController.getAngle();
    }

    @Override
    public int getRoundedAngle() { return Math.round(this.getAngle()); }

    @Override
    public ColorType getColor() {
        return this.colorController.getColorType();
    }

    @Override
    public void setSpeed(int speed) {
        this.motorController.setSpeed(speed);
    }

    @Override
    public void Beep() {

    }

    @Override
    public void startListeningDistance() {
        this.distanceController.startListening();
    }

    @Override
    public void stopListeningDistance() {
        this.distanceController.stopListening();
    }

    @Override
    public void startListeningColor() {
        this.colorController.startListening();
    }

    @Override
    public void stopListeningColor() {
        this.colorController.stopListening();
    }

    @Override
    public void startListeningCompass() {
        this.compassController.startListening();
    }

    @Override
    public void stopListeningCompass() {
        this.compassController.stopListening();
    }

    @Override
    public void distanceChanged(float distance) {
        this.notifyObsevers(RobotMessage.DistanceChanged, Float.toString(distance));
    }
}