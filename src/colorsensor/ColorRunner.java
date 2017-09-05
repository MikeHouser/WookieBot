package colorsensor;

import shared.ColorType;
import shared.CustomColor;
import util.ConsoleHelper;
import util.CustomLogger;
import util.LoggingLevel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ColorRunner extends CustomLogger implements Runnable, IColorObservable {
    // Private fields
    private volatile boolean stopRequested;
    private volatile boolean isRunning = false;

    private ColorType currentColorType = ColorType.NONE;

    public ColorType getCurrentColorType() {
        return this.currentColorType;
    }

    private CustomColor currentCustomColor = new CustomColor(0, 0, 0, 0);

    protected List<IColorObserver> colorObservers = new ArrayList<IColorObserver>();
    protected List<IColorObserver> colorObserversToRemove = new ArrayList<IColorObserver>();

    private boolean finishUnsubscribeToColorChangeCalled = false;

    private IColorSensor colorSensor;

    private final boolean CONSOLE_OUTPUT = true;

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
        super.log("Shutdown has been called.");

        this.stopRequested = true;
    }

    public void readData() {
        super.log("Started listening on color data");

        while (!this.stopRequested) {
            this.isRunning = true;
            ColorType newColorType = this.colorSensor.getColorType();
            this.setColor(newColorType);
            // TODO sleep for ev3
        }
        this.isRunning = false;
        this.stopRequested = false;

        super.log("Shutdown complete");

        this.finishUnsubscribeToColorChangeCalled = true;
    }

    private void setColor(ColorType newColorType) {
        if (this.currentColorType != newColorType) {
            this.currentColorType = newColorType;
            super.log(String.format(
                    "{s} >> New color detected: {s}",
                    this.getClass().getName(),
                    newColorType.name()));
        }

        try {
            for (IColorObserver colorObserver : this.colorObservers) {
                colorObserver.colorChanged(newColorType);
            }
        } catch (java.util.ConcurrentModificationException concurrentModificationException2) {
            super.log(concurrentModificationException2.toString(), LoggingLevel.Error);
        }

        if (this.finishUnsubscribeToColorChangeCalled) {
            this.colorObservers.removeAll(this.colorObserversToRemove);
            this.colorObserversToRemove.clear();

            this.finishUnsubscribeToColorChangeCalled = false;
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
