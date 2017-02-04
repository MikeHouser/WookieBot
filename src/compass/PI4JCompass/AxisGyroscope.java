package compass.PI4JCompass;

import java.io.IOException;


public class AxisGyroscope implements Gyroscope {

    private MultiAxisGyro multiAxisGyro;

    private int trigger;

    private int value;
    private int offset;
    private float angle;

    private float degPerSecondFactor;
    private boolean factorSet = false;

    public AxisGyroscope(MultiAxisGyro multiAxisGyro) {
        this.multiAxisGyro = multiAxisGyro;
    }

    public AxisGyroscope(MultiAxisGyro multiAxisGyro, float degPerSecondFactor) {
        this.multiAxisGyro = multiAxisGyro;
        this.degPerSecondFactor = degPerSecondFactor;
        factorSet = true;
    }

    public void setRawValue(int value) {
        this.value = value;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void setReadTrigger(int trigger) {
        this.trigger = trigger;
    }

    protected float readAndUpdateAngle() throws IOException {
        multiAxisGyro.readGyro();
        int adjusted = ((value - offset) / 40) * 40;
        //int adjusted = value - offset;
        float angularVelocity;
        if (factorSet) {
            angularVelocity = adjusted / degPerSecondFactor;
        } else {
            angularVelocity = adjusted;
        }
        angle = angle + angularVelocity * multiAxisGyro.getTimeDelta() / 1000f;
        return angularVelocity;
    }

    @Override
    public int getRawValue() throws IOException {
        if (trigger == GET_RAW_VALUE_TRIGGER_READ) {
            readAndUpdateAngle();
        }
        return value;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public float getAngle() throws IOException {
        if (trigger == GET_ANGLE_TRIGGER_READ) {
            readAndUpdateAngle();
        }
        return angle;
    }

    @Override
    public float getAngularVelocity() throws IOException {
        if (trigger == GET_ANGULAR_VELOCITY_TRIGGER_READ) {
            return (float)readAndUpdateAngle();
        } else {
            int adjusted = value - offset;
            if (factorSet) {
                return adjusted / degPerSecondFactor;
            } else {
                return adjusted;
            }
        }
    }

    @Override
    public void recalibrateOffset() throws IOException {
        multiAxisGyro.recalibrateOffset();
    }

}

