package colorsensor;

import shared.ColorType;

public interface IColorObserver {
    void colorChanged(ColorType colorType);
}
