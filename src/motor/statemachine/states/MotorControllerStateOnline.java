package motor.statemachine.states;

import motor.MotorController;

public class MotorControllerStateOnline extends MotorControllerState {
    @Override
    public void shutdownMotorController(MotorController mCtrlCtx) {
        super.startMotorController(mCtrlCtx);

        mCtrlCtx.setMotorControllerState(new MotorControllerStateOffline());
    }

    @Override
    public void setSpeed(MotorController mCtrlCtx, int speed) {
        super.setSpeed(mCtrlCtx, speed);

        mCtrlCtx.getAxis().setSpeed(speed);
    }
}
