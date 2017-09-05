package motor.statemachine.states;

import motor.MotorController;

public class MotorControllerStateOffline extends MotorControllerState {
    @Override
    public void startMotorController(MotorController mCtrlCtx) {
        super.startMotorController(mCtrlCtx);

        mCtrlCtx.setMotorControllerState(new MotorControllerStateStopped());
    }
}
