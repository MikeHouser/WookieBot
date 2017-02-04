package colorsensor;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import shared.ColorType;

public class MyEV3ColorSensor implements IColorSensor {

    private EV3ColorSensor ev3ColorSensor;

    public MyEV3ColorSensor(Port sensorPort) {
        this.ev3ColorSensor = new EV3ColorSensor(sensorPort);
        this.ev3ColorSensor.setCurrentMode("ColorID");
    }

    @Override
    public void init() {

    }

    @Override
    public void deInit() {
        ev3ColorSensor.close();
    }

    @Override
    public ColorType getColorType() {
        int colorId = this.ev3ColorSensor.getColorID();
        return this.getColorType(colorId);
    }

    private ColorType getColorType(int colorId) {
        switch (colorId) {
            case 0 : { return ColorType.RED; }
            case 1 : { return ColorType.GREEN; }
            case 2 : { return ColorType.BLUE; }
            case 3 : { return ColorType.YELLOW; }
            case 4 : { return ColorType.MAGENTA; }
            case 5 : { return ColorType.ORANGE; }
            case 6 : { return ColorType.WHITE; }
            case 7 : { return ColorType.BLACK; }
            case 8 : { return ColorType.PINK; }
            case 9 : { return ColorType.GRAY; }
            case 10 : { return ColorType.LIGHT_GRAY; }
            case 11 : { return ColorType.DARK_GRAY; }
            case 12 : { return ColorType.CYAN; }
            case 13 : { return ColorType.BROWN; }
            default: { return ColorType.NONE; }
        }
    }
}
