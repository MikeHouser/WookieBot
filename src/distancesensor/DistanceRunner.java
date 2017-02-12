package distancesensor;

import config.RobotConfig;
import util.ConsoleHelper;

import java.util.ArrayList;
import java.util.List;

public class DistanceRunner implements Runnable, IDistanceObservable {
    private IDistanceSensor sensor;
    private volatile boolean stopRequested;
    private volatile boolean isRunning = false;

    protected List<IDistanceObserver> observers = new ArrayList<IDistanceObserver>();
    protected List<IDistanceObserver> observersToRemove = new ArrayList<IDistanceObserver>();

    private boolean finishUnsubscribeCalled = false;
    private float distance = 0;

    public DistanceRunner(IDistanceSensor sensor) {
        this.sensor = sensor;
    }

    public boolean IsRunning() {
        return this.isRunning;
    }

    @Override
    public void run() {
        ConsoleHelper.printlnDefault("DistanceRunner: Started listening on distance data");
        try {
            while(!this.stopRequested) {
                this.isRunning = true;

                float distance = this.sensor.measureDistance();
                if(distance>=0) {
                    this.setDistance(distance);
                }

                Thread.sleep(RobotConfig.getDistanceSensorSleepMs());
            }
            this.isRunning = false;
            this.stopRequested = false;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        ConsoleHelper.printlnDefault("DistanceRunner: Shutdown complete");

        this.finishUnsubscribeCalled = true;
    }

    private void setDistance(float distance) {
        if(this.distance != distance) {
            this.distance = distance;
            ConsoleHelper.printlnDefault("DistanceRunner: Distance changed -> " + distance);
            try {
                for(IDistanceObserver observer: this.observers) {
                    observer.distanceChanged(distance);
                }
            } catch(java.util.ConcurrentModificationException concurrentModificationException2) {
                System.out.println(concurrentModificationException2);
            }

            if(this.finishUnsubscribeCalled) {
                this.observers.removeAll(this.observersToRemove);
                this.observersToRemove.clear();

                this.finishUnsubscribeCalled = false;
            }
        }
    }

    public void requestStop() {
        ConsoleHelper.printlnDefault("DistanceRunner: Shutdown has been called.");
        if(!this.isRunning) {
            ConsoleHelper.printlnDefault("DistanceRunner: Currently not running - No need for shutdown.");
        }
        this.stopRequested = true;
    }

    @Override
    public void subscribeToDistanceChange(IDistanceObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void unsubscribeToDistanceChange(IDistanceObserver observer) {
        this.observersToRemove.add(observer);
    }

    @Override
    public void finishUnsubscribeToDistanceChange() {
        this.finishUnsubscribeCalled = true;
    }
}
