package lejosextensions;

import lejos.robotics.Color;

/**
 * Created by Mike on 06.11.16.
 */
public class ColorHelper {

    public static String getColorName(int colorId) {
        switch (colorId) {
            case 0 : { return "RED"; }
            case 1 : { return "GREEN"; }
            case 2 : { return "BLUE"; }
            case 3 : { return "YELLOW"; }
            case 4 : { return "MAGENTA"; }
            case 5 : { return "ORANGE"; }
            case 6 : { return "WHITE"; }
            case 7 : { return "BLACK"; }
            case 8 : { return "PINK"; }
            case 9 : { return "GRAY"; }
            case 10 : { return "LIGHT_GRAY"; }
            case 11 : { return "DARK_GRAY"; }
            case 12 : { return "CYAN"; }
            case 13 : { return "BROWN"; }
            default: { return "NONE"; }
        }
    }
}
