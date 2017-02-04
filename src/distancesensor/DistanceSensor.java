package distancesensor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class DistanceSensor implements IDistanceSensor{

    private final static float SOUND_SPEED = 340.29f;  // speed of sound in m/s

    private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration of 10 micro s

    private final static int TIMEOUT_START = 600000; // in nano seconds
    private final static int TIMEOUT_END = 7000000; // in nano seconds ... a little bit more than one meter will me measured

    private GpioController gpio;
    private GpioPinDigitalInput echoPin;
    private GpioPinDigitalOutput trigPin;

    public DistanceSensor() {
        this.gpio = GpioFactory.getInstance();
        this.echoPin = this.gpio.provisionDigitalInputPin(RaspiPin.GPIO_05);
        this.trigPin = this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
    }

    @Override
    public void init() {
        this.trigPin.low();
    }

    @Override
    public void deInit() {
        this.gpio.shutdown();
    }

    @Override
    public float measureDistance() {
        try {
            this.triggerSensor();
            this.waitForSignal();
            long duration = this.measureSignal();

            return duration * SOUND_SPEED / (2 * 10000);
        } catch (TimeoutException e) {
            return -1;
        }
    }

    private void triggerSensor() {
        try {
            this.trigPin.high();
            Thread.sleep( 0, TRIG_DURATION_IN_MICROS * 1000 );
            this.trigPin.low();
        } catch (InterruptedException ex) {
            System.err.println( "Interrupt during trigger" );
        }
    }

    private void waitForSignal() throws TimeoutException {
        long start = System.nanoTime();
        long diff = 0;
        while(this.echoPin.isLow()) {
            diff = System.nanoTime() - start;
            if (diff >= TIMEOUT_START) {
                throw new TimeoutException( "Timeout waiting for signal start" );
            }
        }
    }

    private long measureSignal() throws TimeoutException {
        long start = System.nanoTime();
        long diff = 0;
        while( this.echoPin.isHigh()) {
            diff = System.nanoTime() - start;
            if (diff >= TIMEOUT_END) {
                throw new TimeoutException( "Timeout waiting for signal end" );
            }
        }
        long end = System.nanoTime();

        return (long)Math.ceil( ( end - start ) / 1000.0 );  // Return micro seconds
    }

    private static class TimeoutException extends Exception {

        private final String reason;

        public TimeoutException( String reason ) {
            this.reason = reason;
        }

        @Override
        public String toString() {
            return this.reason;
        }
    }
}
