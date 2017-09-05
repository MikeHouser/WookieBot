package statemachine.linefollower;

import shared.ColorType;
import statemachine.RobotState;
import statemachine.RobotStateContext;

public class StopMovementAndFindLineTimeBased extends StopMovementAndFindLine {

    private int timeoutMs;

    public StopMovementAndFindLineTimeBased(ColorType colorType, boolean turnLeft, int timeoutMs) {
        super(colorType, turnLeft);

        this.timeoutMs = timeoutMs;
    }

    @Override
    public synchronized void transition(RobotStateContext context) {
        if (super.transitionStarted) return;
        super.transitionStarted = true;

        RobotState newState = new FindLineStateTimeBased(this.timeoutMs, super.colorType, super.turnLeft);

        context.setState(newState);
    }
}
