package compass;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import compass.PI4JCompass.Gyroscope;
import compass.PI4JCompass.HMC5883L;

import java.io.IOException;

public class CompassProxyPI4J implements ICompassProxy {

    private HMC5883L hmc5883l = null;
    private float SCALE = 0.92f;

    @Override
    public CompassResult getCompassData(float xOffset, float yOffset) {
        CompassResult result = new CompassResult();
        try {
            // Init compass instance on first use
            if (this.hmc5883l == null) {
                I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
                this.hmc5883l = new HMC5883L(bus);
                hmc5883l.init(hmc5883l.X, Gyroscope.GET_RAW_VALUE_TRIGGER_READ);
            }

            result.xAxisRaw = this.hmc5883l.X.getRawValue();
            result.yAxisRaw = this.hmc5883l.Y.getRawValue();
            result.zAxisRaw = this.hmc5883l.Z.getRawValue();

            result.xAxis = (result.xAxisRaw  - xOffset) * SCALE;
            result.yAxis = (result.yAxisRaw  - yOffset) * SCALE;
            result.zAxis = result.zAxisRaw * SCALE;

            double heading = Math.atan2(result.yAxis , result.xAxis);
            if (heading < 0)
                heading += (2 * Math.PI);

            result.angle = (float)(heading * 180 / Math.PI);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
        }
        return result;
    }
}
