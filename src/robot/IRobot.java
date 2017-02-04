package robot;

import shared.ColorType;

public interface IRobot {
    float getAngle();
    int getRoundedAngle();
    ColorType getColor();

	void calibrateCompass();
    void initRobot();
    void deinitRobot();
	void performCalibrationDrive();
    void startTurnLeft();
    void stopTurnLeft();
    void startTurnRight();
    void stopTurnRight();
    void startDriveForward();
    void stopDriveForward();
    void startDriveBackward();
    void stopDriveBackward();

    void setSpeed(int speed);
    void Beep();

    void startListeningDistance();
    void stopListeningDistance();
    void startListeningColor();
    void stopListeningColor();
    void startListeningCompass();
    void stopListeningCompass();
}
