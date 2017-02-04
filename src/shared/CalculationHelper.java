package shared;

public class CalculationHelper {

    public static int addDegrees(int currentDegrees, int offset) {
        int tempResult = currentDegrees + offset;
        if(tempResult > 360) {
            tempResult = Math.round(tempResult % 360);
        } else if (tempResult < 0) {
            tempResult = Math.round(tempResult % 360);
            tempResult = 360 - Math.abs(tempResult);
        }
        return tempResult;
    }

    public static String toHex(int i)
    {
        String s = Integer.toString(i, 16).toUpperCase();
        while (s.length() % 2 != 0)
            s = "0" + s;
        return "0x" + s;
    }
}
