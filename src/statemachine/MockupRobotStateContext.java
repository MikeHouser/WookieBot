package statemachine;

import colorsensor.MockupColorSensor;
import compass.MockupCompassProxy;
import robot.RobotMessage;
import shared.ColorType;
import shared.UserCommandContainer;

public class MockupRobotStateContext extends RobotStateContext {

    private boolean isFollowingLine = false;

    public MockupRobotStateContext() {
        super();
    }

    @Override
    public void forwardCommand(UserCommandContainer command) {
        switch (command.userCommand) {
            case START_LINE: {
                // Follow line command from user received
                this.isFollowingLine = true;
                MockupColorSensor.colorType = ColorType.BLACK;
            }
            case STOP_LINE: {
                this.isFollowingLine = false;
            }
        }
        super.forwardCommand(command);
    }

    @Override
    public void handleRobotMessage(RobotMessage message, String data) {
        switch (message) {
            case TurnLeftStarted: {
                MockupCompassProxy.angleChangeValue = -1;
                break;
            }
            case TurnLeftStopped: {
                MockupCompassProxy.angleChangeValue = 0;
                break;
            }
            case TurnRightStarted: {
                MockupCompassProxy.angleChangeValue = 1;
                break;
            }
            case TurnRightStopped: {
                MockupCompassProxy.angleChangeValue = 0;
                break;
            }
        }
        super.handleRobotMessage(message, data);
    }
}
