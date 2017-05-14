package statemachine.linefollower;

import config.RobotConfig;
import robot.IRobot;
import robot.RobotMessage;
import shared.ColorType;
import statemachine.RobotState;
import statemachine.RobotStateContext;

public class FindLineState extends RobotState {

    protected ColorType colorType;
    protected boolean turnLeft = true;
    protected boolean stopInit = false;

    public FindLineState(ColorType colorType, boolean turnLeft) {
        this.colorType = colorType;
        this.turnLeft = turnLeft;
    }

    @Override
    public String getName() {
        return String.format("Find line -> turnLeft %s | colorType %s", this.turnLeft, this.colorType);
    }

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        this.stopInit = false;

        IRobot robot = context.getRobot();

        // Set speed
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
    public void transition(RobotStateContext context) {
        IRobot robot = context.getRobot();

        if (this.turnLeft) robot.stopTurnLeft();
        else robot.stopTurnRight();
        robot.setSpeed(RobotConfig.getDefaultMotorSpeedInPercent());

        robot.Beep();

        // New state
        context.setState(new FollowLineState(this.colorType));
    }

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        super.handleRobotMessage(context, message, data);

        switch (message) {
            case ColorChanged: {
                ColorType newColorType = ColorType.valueOf(data);
                if(newColorType == this.colorType) {
                    this.colorFound(context);
                }
            }
        }
    }

    public void colorFound(RobotStateContext context) {
        this.transition(context);
    }

}
