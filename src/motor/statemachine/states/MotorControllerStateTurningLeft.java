package motor.statemachine.states;

import motor.MotorController;

public class MotorControllerStateTurningLeft extends MotorControllerStateOnline {

    @Override
    public void initState(MotorController mCtrlCtx) {
        super.initState(mCtrlCtx);

        mCtrlCtx.getAxis().startTurnLeft();
    }

    @Override
    public void stop(MotorController mCtrlCtx) {
        super.stop(mCtrlCtx);

        mCtrlCtx.getAxis().stop();

        this.doDefaultTransition(mCtrlCtx);
    }

    @Override
    public void doDefaultTransition(MotorController mCtrlCtx) {
        super.doDefaultTransition(mCtrlCtx);

        mCtrlCtx.setMotorControllerState(new MotorControllerStateStopped());
    }
}