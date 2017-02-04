package compass;

public class CompassResult {
	public float angle = 0;
	public float xAxis = 0;
	public float yAxis = 0;
	public float zAxis = 0;
	public float xAxisRaw = 0;
	public float yAxisRaw = 0;
	public float zAxisRaw = 0;
	
	public CompassResult(float angle, float xAxis, float yAxis, float zAxis, float xAxisRaw, float yAxisRaw, float zAxisRaw) {
		this.angle = angle;
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.zAxis = zAxis;
		this.xAxisRaw = xAxisRaw;
		this.yAxisRaw = yAxisRaw;
		this.zAxisRaw = zAxisRaw;
	}

	public CompassResult() {

	}
}