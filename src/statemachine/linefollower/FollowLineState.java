package statemachine.linefollower;

import config.RobotConfig;
import robot.IRobot;
import robot.RobotMessage;
import shared.ColorType;
import statemachine.RobotState;
import statemachine.RobotStateContext;
import util.ConsoleHelper;

import java.text.MessageFormat;

public class FollowLineState extends RobotState {
    private ColorType colorType;
    private final boolean CONSOLE_OUTPUT = true;

    public FollowLineState(ColorType colorType) {
        this.colorType = colorType;
    }

    @Override
    public String getName() {
        return "Follow line";
    }

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        this.performAction(context);
    }

    private void performAction(RobotStateContext context) {
        IRobot robot = context.getRobot();

        robot.setSpeed(RobotConfig.getSearchMotorSpeedInPercent());
        robot.startDriveForward();
    }

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        super.handleRobotMessage(context, message, data);

        switch (message) {
            case ColorChanged: {
                ColorType newColor = ColorType.valueOf(data);
                if (newColor != this.colorType) {
                    if (CONSOLE_OUTPUT) {
                        ConsoleHelper.printlnPurple(MessageFormat.format(
                                "Lost the color to follow {0} and found {1} instead.",
                                this.colorType, newColor));
                    }

                    this.transition(context);
                }
            }
        }
    }

    @Override
    public void transition(RobotStateContext context) {
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
