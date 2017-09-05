package statemachine.common;

import robot.RobotMessage;
import statemachine.RobotState;
import statemachine.RobotStateContext;
import util.ConsoleHelper;

public class OfflineState extends RobotState {

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        switch (message) {
            case Initialized: {
                context.setState(new OnlineState());
            }
        }
    }

    @Override
    public void startRobot(RobotStateContext context) {
        super.startRobot(context);

        context.getRobot().initRobot();
    }
}
