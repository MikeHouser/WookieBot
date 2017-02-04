package colorsensor;

import config.RobotConfig;
import shared.ColorType;
import shared.CustomColor;
import util.ConsoleHelper;

import java.util.ArrayList;
import java.util.List;

public class ColorRunner implements Runnable, IColorObservable {
    // Private fields
    private volatile boolean stopRequested;
    private volatile boolean isRunning = false;

    private ColorType currentColorType = ColorType.NONE;
    public ColorType getCurrentColorType() { return this.currentColorType; }

    private CustomColor currentCustomColor = new CustomColor(0, 0, 0, 0);

    protected List<IColorObserver> colorObservers = new ArrayList<IColorObserver>();
    protected List<IColorObserver> colorObserversToRemove = new ArrayList<IColorObserver>();

    private boolean finishUnsubscribeToColorChangeCalled = false;

    private IColorSensor colorSensor;

    public ColorRunner(IColorSensor colorSensor) {
        this.colorSensor = colorSensor;
    }

    public boolean IsRunning() {
        return this.isRunning;
    }

    public void run() {
        this.readData();
    }

    public void requestStop() {
        System.out.println("ColorRunner: Shutdown has been called.");
        this.stopRequested = true;
    }

    public void readData() {
        System.out.println("ColorRunner: Started listening on color data");

        while (!this.stopRequested) {
            this.isRunning = true;
            ColorType newColorType = this.colorSensor.getColorType();
            this.setColor(newColorType);
            // TODO sleep for ev3
        }
        this.isRunning = false;
        this.stopRequested = false;

        System.out.println("ColorRunner: Shutdown complete");

        this.finishUnsubscribeToColorChangeCalled = true;
    }

    private void setColor(ColorType newColorType) {
        if (this.currentColorType != newColorType) {
            this.currentColorType = newColorType;
            ConsoleHelper.printlnDefault("New color detected: " + newColorType.name());

            try {
                for (IColorObserver colorObserver : this.colorObservers) {
                    colorObserver.colorChanged(newColorType);
                }
            } catch (java.util.ConcurrentModificationException concurrentModificationException2) {
                System.out.println(concurrentModificationException2);
            }

            if (this.finishUnsubscribeToColorChangeCalled) {
                this.colorObservers.removeAll(this.colorObserversToRemove);
                this.colorObserversToRemove.clear();

                this.finishUnsubscribeToColorChangeCalled = false;
            }
        }
    }

    @Override
    public void subscribeToColorChange(IColorObserver observer) {
        this.colorObservers.add(observer);
    }

    @Override
    public void unsubscribeToColorChange(IColorObserver observer) {
        this.colorObserversToRemove.add(observer);
    }

    @Override
    public void finishUnsubscribeToColorChange() {
        this.finishUnsubscribeToColorChangeCalled = true;
    }
}
