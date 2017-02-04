package statemachine.common;

import robot.RobotMessage;
import statemachine.RobotState;
import statemachine.RobotStateContext;
import util.ConsoleHelper;

/**
 * Created by Mike on 27.07.16.
 */
public class OfflineState extends RobotState {

    @Override
    public String getName() {
        return "Offline";
    }

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
