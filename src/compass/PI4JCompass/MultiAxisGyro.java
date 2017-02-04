package compass.PI4JCompass;

import java.io.IOException;

public interface MultiAxisGyro {

    Gyroscope init(Gyroscope triggeringAxis, int triggeringMode) throws IOException;

    void enable() throws IOException;
    void disable() throws IOException;

    void readGyro() throws IOException;

    int getTimeDelta();

    void recalibrateOffset() throws IOException;
}
