package motor.statemachine.states;

import motor.MotorController;

public class MotorControllerStateStopped extends MotorControllerStateOnline {
    @Override
    public void startDriveForward(MotorController mCtrlCtx) {
        super.startDriveForward(mCtrlCtx);

        mCtrlCtx.setMotorControllerState(new MotorControllerStateDrivingForward());
    }

    @Override
    public void startDriveBackward(MotorController mCtrlCtx) {
        super.startDriveBackward(mCtrlCtx);

        mCtrlCtx.setMotorControllerState(new MotorControllerStateDrivingBackward());
    }

    @Override
    public void startTurnLeft(MotorController mCtrlCtx) {
        super.startTurnLeft(mCtrlCtx);

        mCtrlCtx.setMotorControllerState(new MotorControllerStateTurningLeft());
    }

    @Override
    public void startTurnRight(MotorController mCtrlCtx) {
        super.startTurnRight(mCtrlCtx);

        mCtrlCtx.setMotorControllerState(new MotorControllerStateTurningRight());
    }

    @Override
    public void turnLeftWithSteps(MotorController mCtrlCtx, int steps) {
        super.turnLeftWithSteps(mCtrlCtx, steps);

        mCtrlCtx.getAxis().turnLeftWithSteps(steps);
    }

    @Override
    public void turnRightWithSteps(MotorController mCtrlCtx, int steps) {
        super.turnRightWithSteps(mCtrlCtx, steps);

        mCtrlCtx.getAxis().turnRightWithSteps(steps);
    }
}
