package colorsensor;

import shared.ColorType;

public class MockupColorSensor implements IColorSensor {

    public static ColorType colorType = ColorType.NONE;

    @Override
    public void init() {

    }

    @Override
    public void deInit() {

    }

    @Override
    public ColorType getColorType() {
        return colorType;
    }
}
