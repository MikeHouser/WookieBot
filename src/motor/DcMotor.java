package motor;

import com.pi4j.io.gpio.*;

public class DcMotor extends MotorBase {
    private GpioPinPwmOutput speedPin;
    private GpioPinDigitalOutput forwardPin;
    private GpioPinDigitalOutput backwardPin;

    public DcMotor(Pin speedPinId, Pin forwardPinId, Pin backwardPinId, MotorType motorType) {
        super(390, 1024, motorType);

        GpioController gpio = GpioFactory.getInstance();

        this.speedPin = gpio.provisionPwmOutputPin(speedPinId, "SPEED", 0);
        this.forwardPin = gpio.provisionDigitalOutputPin(forwardPinId, "FORWARD", PinState.LOW);
        this.backwardPin = gpio.provisionDigitalOutputPin(backwardPinId, "BACKWARD", PinState.LOW);

        this.speedPin.setShutdownOptions(true);
        this.forwardPin.setShutdownOptions(true);
        this.backwardPin.setShutdownOptions(true);
    }

    @Override
    public void forward() {
        super.forward();

        this.speedPin.setPwm(this.currentSpeed);
        this.forwardPin.high();
        this.backwardPin.low();
    }

    @Override
    public void backward() {
        super.backward();

        this.speedPin.setPwm(this.currentSpeed);
        this.forwardPin.low();
        this.backwardPin.high();
    }

    @Override
    public void stop() {
        super.stop();

        this.speedPin.setPwm(0);
        this.forwardPin.low();
        this.backwardPin.low();
    }
}
