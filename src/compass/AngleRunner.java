package compass;

import config.RobotConfig;
import util.CustomLogger;

import java.util.ArrayList;
import java.util.List;

public class AngleRunner extends CustomLogger implements Runnable, IAngleObservable {
	// Private fields
	private ICompassProxy compassProxy;
    private volatile boolean stopRequested;
	private volatile boolean isRunning = false;
	private float angle = 360;

	public float offsetX = 0;
	public float offsetY = 0;

	protected List<IAngleObserver> angleObservers = new ArrayList<IAngleObserver>();
    protected List<IAngleObserver> angleObserversToRemove = new ArrayList<IAngleObserver>();

    private boolean finishUnsubscribeToAngleChangeCalled = false;
	
	public AngleRunner(ICompassProxy compassProxy) {
		this.compassProxy = compassProxy;
	}

	public boolean isRunning() {
		return this.isRunning;
	}
	
	public void run() {
		this.readData();
	}

    public void requestStop() {
        super.log("Shutdown has been called.");
		if(!this.isRunning) {
			super.log("Currently not running - No need for shutdown.");
		}
        this.stopRequested = true;
    }

	public void readData() {
        super.log("Started listening on compassController data");
		try {
	    	while(!this.stopRequested) {
				this.isRunning = true;

	    		CompassResult compassResult = this.compassProxy.getCompassData(this.offsetX, this.offsetY);
				CompassResultInfo compassResultInfo = CompassResultInfo.CreateCompassResultInfo(compassResult);

				float roundedAngle = compassResultInfo.getAngleRounded();
				this.setAngle(roundedAngle);

	    		Thread.sleep(RobotConfig.getCompassSleepMs());
	    	}
			this.isRunning = false;
	    	this.stopRequested = false;
	    }
	    catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	    super.log("Shutdown complete");

        this.finishUnsubscribeToAngleChangeCalled = true;
	}

	private void setAngle(float angle) {
		if(this.angle != angle) {
			this.angle = angle;
			//ConsoleHelper.printlnDefault("AngleRunner: Angle changed: " + angle);
            try {
                for(IAngleObserver angleObserver: this.angleObservers) {
                    angleObserver.angleChanged(angle);
                }
            } catch(java.util.ConcurrentModificationException concurrentModificationException2) {
                System.out.println(concurrentModificationException2);
            }

            if(this.finishUnsubscribeToAngleChangeCalled) {
                this.angleObservers.removeAll(this.angleObserversToRemove);
                this.angleObserversToRemove.clear();

                this.finishUnsubscribeToAngleChangeCalled = false;
            }
		}
	}

    @Override
	public void subscribeToAngleChange(IAngleObserver observer) {
		this.angleObservers.add(observer);
	}

    @Override
	public void unsubscribeToAngleChange(IAngleObserver observer) {
        this.angleObserversToRemove.add(observer);
	}

    @Override
    public void finishUnsubscribeToAngleChange() {
        this.finishUnsubscribeToAngleChangeCalled = true;
    }
}
