package config;

import java.util.ResourceBundle;

public class RobotConfig {

    private static ResourceBundle rb = ResourceBundle.getBundle("robotconfig");

    private static boolean getBoolean(String key) {
       return Boolean.parseBoolean(rb.getString(key));
    }

    public static boolean getUseLegoRobot() {
        return getBoolean("UseLegoRobot");
    }

    public static boolean getUseMakerRobot() {
        return getBoolean("UseMakerRobot");
    }

    public static boolean getUseMockupRobot() {
        return getBoolean("UseMockupRobot");
    }

    // Robot - Ev3
    public static String IpAddressRobot = "10.0.1.1";

    // Compass
    public static boolean Calibrate_Compass = true;
    public static int Compass_Sleep_MS = 100;

    // General
    public static int FindLine_InitalMove_MS = 250;
    public static int CalibrationDriveDuration_MS = 25000;
    public static int CalibrationDrivePause_MS = 1000;
    public static boolean Write_to_Console = true;

    // DcMotor
    public static int DefaultMotorSpeed = 1; // % minimum
    public static int SearchMotorSpeed = 1; // % minimum
    public static int TurnMotorSpeed = 1; // % minimum
    // DcMotor - EV3
    public static Character MotorType = 'L';
    public static String LeftMotorPort = "C";
    public static String RightMotorPort = "B";
    public static boolean BRAKE_ON_STOP = true;

    // Color sensor
    // Color sensor - EV3
    public static String ColorSensorPort = "S1";

    // Distance sensor
    public static int Distance_Sleep_MS = 100;
}
