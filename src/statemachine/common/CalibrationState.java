package statemachine.common;

import robot.RobotMessage;
import statemachine.RobotState;
import statemachine.RobotStateContext;
import util.ConsoleHelper;

/**
 * Created by Mike on 29.07.16.
 */
public class CalibrationState extends RobotState {

    @Override
    public String getName() {
        return "Calibration";
    }

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        context.getRobot().calibrateCompass();
    }

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        super.handleRobotMessage(context, message, data);

        switch (message) {
            case CalibrationFinished: {
                context.setCalibrationDone(true);
                context.setState(new OnlineState());
                break;
            }
        }
    }
}
