package compass;

public class CalibrationRunner implements Runnable {

	private ICompassProxy compassProxy;

	private volatile boolean stopRequested = false;
	private volatile boolean isRunning = false;
	
	public float offsetX = 0;
	public float offsetY = 0;
	
	public CalibrationRunner(ICompassProxy compassProxy) {
		this.compassProxy = compassProxy;
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void run() {
		this.calibrate();
	}

	public void requestStop() {
		System.out.println("CalibrationRunner: Shutdown has been called.");
		this.stopRequested = true;
	}

	public void calibrate() {
		try {
	    	System.out.println("Calibrate: Started listening on compassController data");
	    	
	    	float minx = 0;
	    	float maxx = 0;
	    	float miny = 0;
	    	float maxy = 0;
	    	
	    	while(!this.stopRequested) {
	    		this.isRunning = true;

	    		CompassResult compassResult = this.compassProxy.getCompassData(0, 0);
	    		if(compassResult.xAxisRaw < minx) {
	    			minx = compassResult.xAxisRaw;
	    		}
	    		if(compassResult.xAxisRaw > maxx) {
	    			maxx = compassResult.xAxisRaw;
	    		}
	    		if(compassResult.yAxisRaw < miny) {
	    			miny = compassResult.yAxisRaw;
	    		}
	    		if(compassResult.yAxisRaw > maxy) {
	    			maxy = compassResult.yAxisRaw;
	    		}
	    		//System.out.println("minx: " + minx + ", maxx: " + maxx + ", miny: " + miny + ", maxy: " + maxy);
	    		Thread.sleep(100);
	    	}
	    	this.offsetX = (minx + maxx) / 2;
	    	this.offsetY = (miny + maxy) / 2;

	    	this.stopRequested = false;
	    	this.isRunning = false;
	    }
	    catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	    System.out.println("Calibrate: Stopped listening on compassController data");
	}

}
