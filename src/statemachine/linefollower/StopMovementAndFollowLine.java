package statemachine.linefollower;

import config.RobotConfig;
import robot.IRobot;
import robot.RobotMessage;
import shared.ColorType;
import statemachine.RobotState;
import statemachine.RobotStateContext;

public class StopMovementAndFollowLine extends LineFollowerState {

    private ColorType colorType;
    private boolean turnLeft;

    public StopMovementAndFollowLine(ColorType colorType, boolean turnLeft) {
        this.colorType = colorType;
        this.turnLeft = turnLeft;
    }

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        if (!context.getRobot().getIsMoving()) {
            this.transition(context);
        } else {
            IRobot robot = context.getRobot();

            // Stop movement
            if (this.turnLeft) robot.stopTurnLeft();
            else robot.stopTurnRight();
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
        if(super.transitionStarted) return;
        super.transitionStarted = true;

        RobotState newState = new FollowLineState(this.colorType);

        context.setState(newState);
    }
}
