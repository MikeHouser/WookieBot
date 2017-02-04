package distancesensor;

import shared.ISensorController;
import util.ConsoleHelper;

public class DistanceController implements IDistanceObservable, ISensorController {
    private IDistanceSensor sensor;

    private int minimumDistance = 5;

    private DistanceRunner distanceRunner;
    private Thread thread = null;

    public DistanceController(IDistanceSensor sensor) {
        this.sensor = sensor;
        this.distanceRunner = new DistanceRunner(sensor);
    }

    public void setMinimumDistance(int minimumDistance) {
        this.minimumDistance = minimumDistance;
    }

    @Override
    public void init() {
        this.sensor.init();
    }

    @Override
    public void deInit() {
        this.sensor.deInit();
    }

    @Override
    public void startListening() {
        if(!this.distanceRunner.IsRunning()) {
            this.thread = new Thread(this.distanceRunner);
            this.thread.start();
        } else {
            ConsoleHelper.printlnRed("DistanceController -> startListening -> Distancerunner is still running.");
        }
    }

    @Override
    public void stopListening() {
        this.distanceRunner.requestStop();
    }

    @Override
    public void subscribeToDistanceChange(IDistanceObserver observer) {
        this.distanceRunner.subscribeToDistanceChange(observer);
    }

    @Override
    public void unsubscribeToDistanceChange(IDistanceObserver observer) {
        this.distanceRunner.unsubscribeToDistanceChange(observer);
    }

    @Override
    public void finishUnsubscribeToDistanceChange() {
        this.distanceRunner.finishUnsubscribeToDistanceChange();
    }
}
