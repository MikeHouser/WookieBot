package statemachine.linefollower;

import config.RobotConfig;
import robot.IRobot;
import robot.RobotMessage;
import shared.ColorType;
import statemachine.RobotStateContext;

public class FindLineStateStepperBased extends FindLineState {

    private int steps = 0;

    /**
     * Constructor
     * @param steps At initial call from other class it should be set to 0
     * @param colorType
     * @param turnLeft
     */
    public FindLineStateStepperBased(int steps, ColorType colorType, boolean turnLeft) {
        super(colorType, turnLeft);

        this.steps = steps;
        this.steps += RobotConfig.getFindLineStepOffset();
    }

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        if (super.stopInit) return;

        IRobot robot = context.getRobot();

        if (this.turnLeft) robot.turnLeftWithSteps(this.steps);
        else robot.turnRightWithSteps(this.steps);
    }

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        super.handleRobotMessage(context, message, data);

        switch (message) {
            case MovementStopped: {
                this.changeSearchDirection(context);
                break;
            }
        }
    }

    private void changeSearchDirection(RobotStateContext context) {
        context.setState(new FindLineStateStepperBased(this.steps, super.colorType, !super.turnLeft));
    }
}
