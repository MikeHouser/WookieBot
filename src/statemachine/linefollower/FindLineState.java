package statemachine.linefollower;

import config.RobotConfig;
import robot.IRobot;
import robot.RobotMessage;
import shared.ColorType;
import shared.UserCommandContainer;
import statemachine.RobotStateContext;

public class FindLineState extends LineFollowerState {

    protected ColorType colorType;
    protected boolean turnLeft = true;
    protected boolean stopInit = false;
    protected boolean colorFound = false;

    public static boolean lastFoundOnLeft = true;

    public FindLineState(ColorType colorType, boolean turnLeft) {
        this.colorType = colorType;
        this.turnLeft = turnLeft;
    }

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        super.log(String.format("turnLeft %s, colorType %s", this.turnLeft, this.colorType));

        this.stopInit = false;

        IRobot robot = context.getRobot();
        robot.setSpeed(RobotConfig.getSearchMotorSpeedInPercent());

        // Check if transition can be done
        if(robot.getColor() == this.colorType) {
            this.transition(context);
            this.stopInit = true;
        }
    }

    @Override
    /**
     * Gets called when the colored line has been found again
     */
    public synchronized void transition(RobotStateContext context) {
        if (super.transitionStarted) return;
        super.transitionStarted = true;

        // New state
        context.setState(new StopMovementAndFollowLine(this.colorType, this.turnLeft));
    }

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        super.handleRobotMessage(context, message, data);

        switch (message) {
            case ColorChanged: {
                ColorType newColorType = ColorType.valueOf(data);
                if(newColorType == this.colorType) {
                    // Cool ... Found the line again
                    this.colorFound = true;
                    lastFoundOnLeft = this.turnLeft;
                    this.colorFound(context);
                }
            }
        }
    }

    @Override
    public void handleCommand(RobotStateContext context, UserCommandContainer command) {
        super.handleCommand(context, command);

        switch (command.userCommand) {
            case STOP_LINE: {

                break;
            }
        }
    }

    public void colorFound(RobotStateContext context) {
        this.transition(context);
    }
}
