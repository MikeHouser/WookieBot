package robot;

import colorsensor.ColorController;
import colorsensor.TCS34725Sensor;
import com.pi4j.io.gpio.RaspiPin;
import compass.CompassController;
import compass.CompassProxyPI4J;
import distancesensor.DistanceController;
import distancesensor.DistanceSensor;
import led.Led;
import motor.*;

public class Tank extends RobotBase {

    @Override
    public void initRobot() {
        super.compassController = new CompassController(new CompassProxyPI4J());

        IMotor leftMotor = new
                StepperMotor28BYJ48(RaspiPin.GPIO_25, RaspiPin.GPIO_24,
                RaspiPin.GPIO_23, RaspiPin.GPIO_22,
                StepperMotor28BYJ48.SteppingMethod.FULL_STEP, MotorType.LEFT);

        IMotor rightMotor = new
                StepperMotor28BYJ48(RaspiPin.GPIO_29, RaspiPin.GPIO_28,
                RaspiPin.GPIO_27, RaspiPin.GPIO_26,
                StepperMotor28BYJ48.SteppingMethod.FULL_STEP, MotorType.RIGHT);

        super.motorController = new MotorController(leftMotor, rightMotor);

        super.distanceController = new DistanceController(new DistanceSensor());

        super.led = new Led(RaspiPin.GPIO_01);

        super.colorController = new ColorController(new TCS34725Sensor());

        super.initRobot();
        super.initRobotComplete();
    }
}
