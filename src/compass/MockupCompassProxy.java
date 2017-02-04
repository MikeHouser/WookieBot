package compass;

public class MockupCompassProxy implements ICompassProxy {

	private float lastAngle = 0;
	public static int angleChangeValue = 0;
	
	public MockupCompassProxy() {
		
	}
	
	public CompassResult getCompassData(float xOffset, float yOffset) {
		this.lastAngle += angleChangeValue;
		if(this.lastAngle>360) this.lastAngle = 0;
        else if(this.lastAngle<0) this.lastAngle = 360;
		
		CompassResult compassResult = new CompassResult(this.lastAngle, 0, 0, 0, 0, 0, 0);
		return compassResult;
	}

}
