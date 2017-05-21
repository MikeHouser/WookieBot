package statemachine.linefollower;

import shared.UserCommandContainer;
import statemachine.RobotState;
import statemachine.RobotStateContext;

public class LineFollowerState extends RobotState {
    public LineFollowerState() {
        super();
    }

    @Override
    public void handleCommand(RobotStateContext context, UserCommandContainer command) {
        super.handleCommand(context, command);

        switch (command.userCommand) {
            case STOP_LINE: {
                super.transitionToOnline(context);
            }
        }
    }
}
