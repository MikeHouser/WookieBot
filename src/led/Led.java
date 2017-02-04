package led;

import com.pi4j.io.gpio.*;

public class Led implements ILed {
    private GpioController gpio; // Default GPIO_01
    private GpioPinDigitalOutput led;
    private Pin pin;

    public Led(Pin pin) {
        this.pin = pin;
    }

    @Override
    public void init() {
        this.gpio = GpioFactory.getInstance();
        this.led = gpio.provisionDigitalOutputPin(this.pin);
    }

    @Override
    public void deInit() {
        this.deactivate();
        this.gpio.shutdown();
    }

    @Override
    public void activate() {
        this.setState(true);
    }

    @Override
    public void deactivate() {
        this.setState(false);
    }

    @Override
    public void setState(boolean activated) {
        this.led.setState(activated);
    }
}
