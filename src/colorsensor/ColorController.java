package colorsensor;

import shared.ColorType;
import shared.ISensorController;
import util.ConsoleHelper;

public class ColorController implements IColorObservable, ISensorController {

    private ColorRunner colorRunner;
    private Thread tColor = null;
    private IColorSensor colorSensor;

    public ColorController(IColorSensor colorSensor) {
        this.colorSensor = colorSensor;
        this.colorRunner = new ColorRunner(this.colorSensor);
    }

    public ColorType getColorType() {
        return this.colorSensor.getColorType();
    }

    @Override
    public void init() {
        this.colorSensor.init();
    }

    @Override
    public void deInit() {
        this.colorSensor.deInit();
    }

    @Override
    public void startListening() {
        if (!this.colorRunner.IsRunning()) {
            this.tColor = new Thread(this.colorRunner);
            this.tColor.start();
        } else {
            ConsoleHelper.printlnRed("ColorController -> startListening -> ColorRunner is still running.");
        }
    }
    @Override
    public void stopListening() {
        this.colorRunner.requestStop();
    }

    @Override
    public void subscribeToColorChange(IColorObserver observer) {
        this.colorRunner.subscribeToColorChange(observer);
    }

    @Override
    public void unsubscribeToColorChange(IColorObserver observer) {
        this.colorRunner.unsubscribeToColorChange(observer);
    }

    @Override
    public void finishUnsubscribeToColorChange() {
        this.colorRunner.finishUnsubscribeToColorChange();
    }
}
