package compass.PI4JCompass;

import java.io.IOException;

public interface Gyroscope {

    int READ_NOT_TRIGGERED = 0;
    int GET_ANGLE_TRIGGER_READ = 1;
    int GET_ANGULAR_VELOCITY_TRIGGER_READ = 2;
    int GET_RAW_VALUE_TRIGGER_READ = 4;

    float getAngularVelocity() throws IOException;

    void recalibrateOffset() throws IOException;

    void setReadTrigger(int readTrigger);

    void setRawValue(int value);
    int getRawValue() throws IOException;

    void setOffset(int offset);
    int getOffset();

    void setAngle(float angle);
    float getAngle() throws IOException;

}
