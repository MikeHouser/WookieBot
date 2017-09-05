package statemachine.linefollower;

import shared.ColorType;
import statemachine.RobotState;
import statemachine.RobotStateContext;

public class StopMovementAndFindLineStepperBased extends StopMovementAndFindLine {

    private int steps;

    public StopMovementAndFindLineStepperBased(ColorType colorType, boolean turnLeft, int steps) {
        super(colorType, turnLeft);

        this.steps = steps;
    }

    @Override
    public synchronized void transition(RobotStateContext context) {
        if (super.transitionStarted) return;
        super.transitionStarted = true;

        RobotState newState = new FindLineStateStepperBased(this.steps, super.colorType, super.turnLeft);

        context.setState(newState);
    }
}
