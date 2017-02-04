package shared;

import util.ConsoleHelper;

import java.awt.*;

public class CustomColor {
    private int rawRed = -1, rawBlue = -1, rawGreen = -1, rawClear = -1;
    private int rgbRed = -1, rgbBlue = -1, rgbGreen = -1;
    private int illuminance = -1;
    private int colorTemperature = -1;

    private Color[] knownColors = new Color[] { Color.WHITE, Color.BLACK /*, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW */ };
    private Color closestColor = Color.BLACK;

    private ColorType colorType = ColorType.NONE;

    public CustomColor(int rawRed, int rawBlue, int rawGreen, int rawClear)
    {
        this.rawRed = rawRed;
        this.rawBlue = rawBlue;
        this.rawGreen = rawGreen;
        this.rawClear = rawClear;

        //ConsoleHelper.printlnDefault("Red: " + this.rawRed + ", Green: " + this.rawGreen + ", Blue: " + this.rawBlue + "Clear: " + this.rawClear);
    }

    public int getRawRed() { return this.rawRed; }
    public int getRawBlue() { return this.rawBlue; }
    public int getRawGreen() { return this.rawGreen; }
    public int getRawClear() { return this.rawClear; }

    public int getRgbRed() { return this.rgbRed; }
    public int getRgbBlue() { return this.rgbBlue; }
    public int getRgbGreen() { return this.rgbGreen; }

    public int getIlluminance() { return this.illuminance; }

    public int getColorTemperature() { return this.colorTemperature; }

    public Color getClosestColor() { return this.closestColor; }

    public ColorType getColorType() { return this.colorType; }

    public void calculateRGB() {
        if( this.rawClear > 0 && this.rawRed >= 0 && this.rawBlue >= 0 && this.rawGreen >= 0) {
            this.rgbRed = this.rawRed * 255 / this.rawClear;
            this.rgbBlue = this.rawBlue * 255 / this.rawClear;
            this.rgbGreen = this.rawGreen * 255 / this.rawClear;

            //ConsoleHelper.printlnDefault("Red: " + this.rgbRed + ", Green: " + this.rgbGreen + ", Blue: " + this.rgbBlue);
        }
    }

    /*
     * Values in Lux (or Lumens) per square meter.
     */
    public void calculateLux()
    {
        double tempIlluminance = (-0.32466 * this.rawRed) + (1.57837 * this.rawGreen) + (-0.73191 * this.rawBlue);
        this.illuminance = (int)tempIlluminance;
    }

    /*
     * Converts the raw R/G/B values to color temperature in degrees Kelvin
     * see http://en.wikipedia.org/wiki/Color_temperature
     */
    public void calculateColorTemperature()
    {
        // 1. Map RGB values to their XYZ counterparts.
        // Based on 6500K fluorescent, 3000K fluorescent
        // and 60W incandescent values for a wide range.
        // Note: Y = Illuminance or lux
        double X = (-0.14282 * this.rawRed) + (1.54924 * this.rawGreen) + (-0.95641 * this.rawBlue);
        double Y = (-0.32466 * this.rawRed) + (1.57837 * this.rawGreen) + (-0.73191 * this.rawBlue);
        double Z = (-0.68202 * this.rawRed) + (0.77073 * this.rawGreen) + ( 0.56332 * this.rawBlue);

        // 2. Calculate the chromaticity co-ordinates
        double xc = (X) / (X + Y + Z);
        double yc = (Y) / (X + Y + Z);

        // 3. Use McCamy's formula to determine the CCT
        double n = (xc - 0.3320) / (0.1858 - yc);

        // Calculate the final CCT
        double cct = (449.0 * Math.pow(n, 3.0)) + (3525.0 * Math.pow(n, 2.0)) + (6823.3 * n) + 5520.33;

        this.colorTemperature = (int)cct;
    }

    public void calculateClosestColor() {
        if (this.rgbRed >= 0 && this.rgbGreen >= 0 && this.rgbBlue >= 0) {
            double minDiff = Double.MAX_VALUE;
            for (Color c : this.knownColors) {
                double diff = Math.pow((c.getRed() - this.rgbRed), 2) +
                        Math.pow((c.getGreen() - this.rgbGreen), 2) +
                        Math.pow((c.getBlue() - this.rgbBlue), 2);
                diff = (int) Math.sqrt(diff);
                if (diff < minDiff) {
                    minDiff = diff;
                    this.closestColor = c;
                }
            }
        }
    }

    public void calculateColorType() {
        int border = 3000;

        if(this.rawRed > border && this.rawBlue > border & this.rawGreen > border) {
            this.colorType = ColorType.WHITE;
        } else {
            this.colorType = ColorType.BLACK;
        }

        /*
        if (this.closestColor == Color.BLACK) {
            this.colorType = ColorType.BLACK;
        } else if (this.closestColor == Color.WHITE) {
            this.colorType = ColorType.WHITE;
        } else if (this.closestColor == Color.RED) {
            this.colorType = ColorType.RED;
        } else if (this.closestColor == Color.BLUE) {
            this.colorType = ColorType.BLUE;
        } else if (this.closestColor == Color.GREEN) {
            this.colorType = ColorType.GREEN;
        } else {
            this.colorType  = ColorType.NONE;
        }
        */
    }
}
