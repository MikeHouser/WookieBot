package colorsensor;

import shared.ColorType;

public class MockupColorSensor implements IColorSensor {

    @Override
    public void init() {

    }

    @Override
    public void deInit() {

    }

    @Override
    public ColorType getColorType() {
        return ColorType.NONE;
    }
}
