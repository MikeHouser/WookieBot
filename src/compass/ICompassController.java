package compass;

public interface ICompassController {
	void startCalibrating();
	void stopCalibrating();
    float getAngle();
}
