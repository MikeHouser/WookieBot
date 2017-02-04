package compass;

/**
 * Created by mike on 19.05.16.
 */
public class CompassResultInfo extends CompassResult {

    public CompassResultInfo(float angle,
        float xAxis, float yAxis, float zAxis,
        float xAxisRaw, float yAxisRaw, float zAxisRaw) {
        super(angle, xAxis, yAxis, zAxis, xAxisRaw, yAxisRaw, zAxisRaw);
    }

    public float getAngleRounded() {
        return Math.round(super.angle);
    }

    public static CompassResultInfo CreateCompassResultInfo(CompassResult compassResult) {
        return new CompassResultInfo(compassResult.angle,
                compassResult.xAxis, compassResult.yAxis, compassResult.zAxis,
                compassResult.xAxisRaw, compassResult.yAxisRaw, compassResult.zAxisRaw);
    }
}
