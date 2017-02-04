package colorsensor;

import shared.ColorType;

public interface IColorSensor {
    void init();
    void deInit();
    ColorType getColorType();
}
