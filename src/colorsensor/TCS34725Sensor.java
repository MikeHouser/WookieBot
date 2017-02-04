package colorsensor;

import shared.ColorType;
import shared.CustomColor;

public class TCS34725Sensor implements IColorSensor {
    private TCS34725 sensor;

    public TCS34725Sensor() {
        this.sensor = new TCS34725(TCS34725.TCS34725_INTEGRATIONTIME_2_4MS, TCS34725.TCS34725_GAIN_1X);
    }

    @Override
    public void init() {

    }

    @Override
    public void deInit() {
        try {
            this.sensor.disable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ColorType getColorType() {
        try {
            CustomColor color = this.getCustomColor();
            //color.calculateRGB();
            //color.calculateClosestColor();
            color.calculateColorType();
            return color.getColorType();
        } catch (Exception e) {
            e.printStackTrace();
            return ColorType.NONE;
        }
    }

    private CustomColor getCustomColor() {
        try {
            return this.sensor.getRawData();
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomColor(0, 0, 0, 0);
        }
    }
}
