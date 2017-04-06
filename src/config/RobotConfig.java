package config;

import java.util.ResourceBundle;

public class RobotConfig {

    private static ResourceBundle rb = ResourceBundle.getBundle("robotconfig");

    //region Helper methods

    private static boolean getBoolean(String key) {
        return Boolean.parseBoolean(rb.getString(key));
    }

    private static int getInt(String key) {
        return Integer.parseInt(rb.getString(key));
    }

    private static Character getCharacter(String key) {
        String temp = rb.getString(key);
        if(temp.length()>0) {
            return temp.charAt(0);
        } else {
            return ' ';
        }
    }

    //endregion

    public static boolean getUseMockupStateContext() { return getBoolean("UseMockupStateContext"); }

    // Robot Type
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
    public static String getIpAddressEv3() {
        return  rb.getString("IpAddressEv3");
    }

    // Compass
    public static boolean getCalibrateCompass() {
        return getBoolean("CalibrateCompass");
    }
    public static int getCompassSleepMs() {
        return getInt("CompassSleepMs");
    }

    // General
    public static int getFindLineInitialMoveMs() {
        return getInt("FindLineInitialMoveMs");
    }
    public static int getCalibrationDriveDurationMs() {
        return getInt("CalibrationDriveDurationMs");
    }
    public static int getCalibrationDrivePauseMs() {
        return getInt("CalibrationDrivePauseMs");
    }
    public static boolean getWriteToConsole() {
        return getBoolean("WriteToConsole");
    }

    // Motor
    public static int getDefaultMotorSpeedInPercent() {
        return getInt("DefaultMotorSpeedInPercent");
    }
    public static int getSearchMotorSpeedInPercent() {
        return getInt("SearchMotorSpeedInPercent");
    }
    public static int getTurnMotorSpeedInPercent() {
        return getInt("TurnMotorSpeedInPercent");
    }
    // Motor - EV3
    public static Character getEv3MotorType() {
        return getCharacter("Ev3MotorType");
    }
    public static String getEv3LeftMotorPort() {
        return rb.getString("Ev3LeftMotorPort");
    }
    public static String getEv3RightMotorPort() {
        return rb.getString("Ev3RightMotorPort");
    }
    public static boolean getMotorBrakeOnStop() {
        return getBoolean("MotorBrakeOnStop");
    }
    // Motor - Stepper
    public static int getStepperMinSpeedAbs() { return getInt("StepperMinSpeedAbs"); }
    public static int getStepperMaxSpeedAbs() { return getInt("StepperMaxSpeedAbs"); }

    // Color sensor
    // Color sensor - EV3
    public static String getEv3ColorSensorPort() {
        return rb.getString("Ev3ColorSensorPort");
    }

    // Distance sensor
    public static int getDistanceSensorSleepMs() {
        return getInt("DistanceSensorSleepMs");
    }

    // Find Line
    public static boolean getFindLineTimeBased()  {
        return getBoolean("FindLineTimeBased");
    }
    public static int getFindLineStepOffset() {
        return getInt("FindLineStepOffset");
    }
}
