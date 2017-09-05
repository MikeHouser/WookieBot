package statemachine.linefollower;

import config.RobotConfig;
import robot.RobotMessage;
import shared.ColorType;
import statemachine.RobotState;
import statemachine.RobotStateContext;

public class StopMovementAndFindLine extends LineFollowerState {

    protected ColorType colorType;
    protected boolean turnLeft;

    public StopMovementAndFindLine(ColorType colorType, boolean turnLeft) {
        this.colorType = colorType;
        this.turnLeft = turnLeft;
    }

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        if (!context.getRobot().getIsMoving()) {
            this.transition(context);
        } else {
            context.getRobot().stopDriveForward();
        }
    }

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        super.handleRobotMessage(context, message, data);

        switch (message) {
            case MovementStopped: {
                this.transition(context);
                break;
            }
        }
    }

    @Override
    public synchronized void transition(RobotStateContext context) {
        if(this.transitionStarted) return;
        this.transitionStarted = true;

        RobotState newState;
        if(RobotConfig.getFindLineTimeBased()) {
            newState = new FindLineStateTimeBased(RobotConfig.getFindLineInitialMoveMs(), this.colorType, FindLineState.lastFoundOnLeft);
        } else {
            newState = new FindLineStateStepperBased(0, this.colorType, FindLineState.lastFoundOnLeft);
        }

        context.setState(newState);
    }
}
