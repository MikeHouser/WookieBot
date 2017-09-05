package statemachine.common;

import config.RobotConfig;
import robot.IRobot;
import shared.CalculationHelper;
import shared.UserCommandContainer;
import statemachine.RobotState;
import statemachine.RobotStateContext;
import statemachine.linefollower.SetLineColorState;

public class OnlineState extends RobotState {

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        if (RobotConfig.getCalibrateCompass() && !context.isCalibrationDone()) {
            context.setState(new CalibrationState());
        }
    }

    @Override
    public void stopRobot(RobotStateContext context) {
        super.stopRobot(context);
    }

    @Override
    public void handleCommand(RobotStateContext context, UserCommandContainer command) {
        super.handleCommand(context, command);

        switch (command.userCommand) {
            case LEFT: {
                this.initiateTurn(context, command.intArg, true);
                break;
            }
            case RIGHT: {
                this.initiateTurn(context, command.intArg, false);
                break;
            }
            case START_LINE: {
                context.setState(new SetLineColorState());
            }
        }
    }

    private void initiateTurn(RobotStateContext context, int offset, boolean left) {
        if (left) {
            offset *= -1;
        }
        IRobot robot = context.getRobot();
        int startAngle = robot.getRoundedAngle();
        int targetAngle = CalculationHelper.addDegrees(robot.getRoundedAngle(), offset);

        super.log(String.format("StartAngle %d / TargetAngle  %d", startAngle, targetAngle));

        if (left) {
            context.setState(new TurnLeftState(targetAngle, offset));
        } else {
            context.setState(new TurnRightState(targetAngle, offset));
        }
    }
}
