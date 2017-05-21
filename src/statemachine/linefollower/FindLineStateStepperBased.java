package statemachine.linefollower;

import config.RobotConfig;
import robot.IRobot;
import robot.RobotMessage;
import shared.ColorType;
import statemachine.RobotState;
import statemachine.RobotStateContext;
import util.ConsoleHelper;

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

        // Calculate steps
        this.steps += RobotConfig.getFindLineStepOffset();
    }

    @Override
    public String getName() {
        return "FindLineStateStepperBased";
    }

    @Override
    public String getDebugInfo() {
        return super.getDebugInfo() + String.format(", steps %d", this.steps);
    }

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        if (super.stopInit) return;

        this.performAction(context);
    }

    private void performAction(RobotStateContext context) {
        IRobot robot = context.getRobot();
        if (this.turnLeft) robot.turnLeftWithSteps(this.steps);
        else robot.turnRightWithSteps(this.steps);
    }

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        super.handleRobotMessage(context, message, data);

        switch (message) {
            case MovementStopped: {
                if(super.colorFound) return;

                this.changeSearchDirectionTransition(context);
                break;
            }
        }
    }

    private synchronized void changeSearchDirectionTransition(RobotStateContext context) {
        if (super.transitionStarted) return;
        super.transitionStarted = true;

        RobotState newState = new StopMovementAndFindLineStepperBased(super.colorType, !super.turnLeft, this.steps);
        context.setState(newState);
    }
}
