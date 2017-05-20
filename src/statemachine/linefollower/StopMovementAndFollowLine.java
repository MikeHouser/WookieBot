package statemachine.linefollower;

import config.RobotConfig;
import robot.IRobot;
import robot.RobotMessage;
import shared.ColorType;
import statemachine.RobotState;
import statemachine.RobotStateContext;

public class StopMovementAndFollowLine extends RobotState {

    private ColorType colorType;
    private boolean transitionStarted = false;
    private boolean turnLeft;

    public StopMovementAndFollowLine(ColorType colorType, boolean turnLeft) {
        this.colorType = colorType;
        this.turnLeft = turnLeft;
    }

    @Override
    public String getName() {
        return "StopMovementAndFollowLine";
    }

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        if (!context.getRobot().getIsMoving()) {
            this.transition(context);
        } else {
            IRobot robot = context.getRobot();

            if (this.turnLeft) robot.stopTurnLeft();
            else robot.stopTurnRight();
            robot.setSpeed(RobotConfig.getSearchMotorSpeedInPercent());

            robot.Beep();
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

        RobotState newState = new FollowLineState(this.colorType);

        context.setState(newState);
    }
}
