package compass;

import shared.ISensorController;
import util.ConsoleHelper;
import util.CustomLogger;

public class CompassController extends CustomLogger implements ICompassController, ISensorController, IAngleObservable {

	private ICompassProxy compassProxy;
	
	private Thread tCalibration = null;
	private Thread tAngle = null;
	
	private CalibrationRunner calibrationRunner;
	private AngleRunner angleRunner;

    private float offsetX = 0;
    private float offsetY = 0;
	
	public CompassController(ICompassProxy compassProxy) {
		this.compassProxy = compassProxy;

		this.calibrationRunner = new CalibrationRunner(this.compassProxy);
		this.angleRunner = new AngleRunner(this.compassProxy);
	}

	@Override
	public void init() {

	}

	@Override
	public void deInit() {

	}

	@Override
	public void startListening() {
		if (!this.angleRunner.isRunning()) {
			this.tAngle = new Thread(this.angleRunner);
			this.tAngle.start();
		} else {
			super.log("startListening -> AngleRunner is still running.");
		}
	}

	@Override
	public void stopListening() {
		this.angleRunner.requestStop();
	}

	@Override
	public void startCalibrating() {
		if (!this.calibrationRunner.isRunning()) {
			this.tCalibration = new Thread(this.calibrationRunner);
			this.tCalibration.start();
		}  else {
			super.log("startCalibrating -> CalibrationRunner is still running.");
		}
	}

	@Override
	public void stopCalibrating() {
	    if(!this.calibrationRunner.isRunning()) return;

		this.calibrationRunner.requestStop();

		super.log("Wait for 'CalibrationRunner' shutdown to complete");

		try {
			this.tCalibration.join();
		} catch (InterruptedException e) { }

        super.log("Shutdown for 'CalibrationRunner' has completed.");

		this.offsetX = this.angleRunner.offsetX = this.calibrationRunner.offsetX;
		this.offsetY = this.angleRunner.offsetY = this.calibrationRunner.offsetY;
	}

	@Override
	public float getAngle() {
		CompassResult compassResult = this.compassProxy.getCompassData(this.offsetX, this.offsetY);
		CompassResultInfo compassResultInfo = CompassResultInfo.CreateCompassResultInfo(compassResult);
		float roundedAngle = compassResultInfo.getAngleRounded();

		return roundedAngle;
	}

    @Override
	public void subscribeToAngleChange(IAngleObserver observer) {
		this.angleRunner.subscribeToAngleChange(observer);
	}

    @Override
	public void unsubscribeToAngleChange(IAngleObserver observer) {
		this.angleRunner.unsubscribeToAngleChange(observer);
	}

    @Override
    public void finishUnsubscribeToAngleChange() {
        this.angleRunner.finishUnsubscribeToAngleChange();
    }
}
