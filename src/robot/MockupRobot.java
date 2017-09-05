package robot;

import colorsensor.ColorController;
import colorsensor.MockupColorSensor;
import compass.CompassController;
import compass.MockupCompassProxy;
import distancesensor.DistanceController;
import distancesensor.MockupDistanceSensor;
import led.MockupLed;
import motor.MockupMotor;
import motor.MotorController;
import motor.MotorType;

public class MockupRobot extends RobotBase {

    private MockupCompassProxy mockupCompassProxy = new MockupCompassProxy();

    @Override
    public void initRobot() {
	    super.compassController = new CompassController(mockupCompassProxy);

	    super.motorController = new MotorController(
	            new MockupMotor(0, 100, MotorType.LEFT),
                new MockupMotor(0, 100, MotorType.RIGHT));

	    super.distanceController = new DistanceController(new MockupDistanceSensor());

	    super.led = new MockupLed();

	    super.colorController = new ColorController(new MockupColorSensor());

        super.initRobot();

        super.initRobotComplete();
    }

    @Override
    public void printMessage(String message) {
        super.printMessage("MockupRobot: ", message);
    }

    @Override
    public void startTurnLeft() {
        super.startTurnLeft();
        this.mockupCompassProxy.angleChangeValue = -1;
    }

    @Override
    public void startTurnRight() {
        super.startTurnRight();
        this.mockupCompassProxy.angleChangeValue = 1;
    }

    @Override
    public void stopTurnLeft() {
        this.mockupCompassProxy.angleChangeValue = 0;
        super.stopTurnLeft();

    }
}
