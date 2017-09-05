package statemachine.linefollower;

import config.RobotConfig;
import robot.IRobot;
import robot.RobotMessage;
import shared.ColorType;
import statemachine.RobotState;
import statemachine.RobotStateContext;

import java.text.MessageFormat;

public class FollowLineState extends LineFollowerState {
    private ColorType colorType;

    public FollowLineState(ColorType colorType) {
        this.colorType = colorType;
    }

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        this.performAction(context);
    }

    private void performAction(RobotStateContext context) {
        IRobot robot = context.getRobot();

        if(!this.checkColor(context, robot.getColor())) {
            return;
        }

        robot.setSpeed(RobotConfig.getDefaultMotorSpeedInPercent());
        robot.startDriveForward();
    }

    private boolean checkColor(RobotStateContext context, ColorType newColor) {
        super.log(MessageFormat.format(
                "Color to check: {0}" , newColor.name()));

        if (newColor != this.colorType) {
            super.log(MessageFormat.format(
                    "Lost the color to follow [{0}] and found [{1}] instead.",
                    this.colorType, newColor));

            this.transition(context);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        super.handleRobotMessage(context, message, data);

        switch (message) {
            case ColorChanged: {
                ColorType newColor = ColorType.valueOf(data);
                this.checkColor(context, newColor);
            }
        }
    }

    @Override
    public synchronized void transition(RobotStateContext context) {
        if (super.transitionStarted) return;
        super.transitionStarted = true;

        RobotState newState;
        if(RobotConfig.getFindLineTimeBased()) {
            newState = new StopMovementAndFindLineTimeBased(this.colorType, FindLineState.lastFoundOnLeft, RobotConfig.getFindLineInitialMoveMs());
        } else {
            newState = new StopMovementAndFindLineStepperBased(this.colorType, FindLineState.lastFoundOnLeft, 0);
        }

        context.setState(newState);
    }

    @Override
    public void stopRobot(RobotStateContext context) {
        context.getRobot().stopDriveForward();

        super.stopRobot(context);
    }
}
