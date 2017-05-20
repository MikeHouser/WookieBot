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
    protected boolean colorFound = false;
    protected boolean foundtransitionStarted = false;
    protected boolean searchTransitionStarted = false;

    public static boolean lastFoundOnLeft = true;

    public FindLineState(ColorType colorType, boolean turnLeft) {
        this.colorType = colorType;
        this.turnLeft = turnLeft;
    }

    @Override
    public String getName() {
        return "Find line";
    }

    @Override
    public String getDebugInfo() {
        return String.format("turnLeft %s, colorType %s", this.turnLeft, this.colorType);
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
    public synchronized void transition(RobotStateContext context) {
        if (this.foundtransitionStarted) return;
        this.foundtransitionStarted = true;

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

    public void colorFound(RobotStateContext context) {
        this.transition(context);
    }

}
