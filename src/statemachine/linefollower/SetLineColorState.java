package statemachine.linefollower;

import robot.IRobot;
import shared.ColorType;
import statemachine.RobotState;
import statemachine.RobotStateContext;
import util.ConsoleHelper;

public class SetLineColorState extends RobotState {

    @Override
    public String getName() {
        return "Set line color";
    }

    @Override
    public void initState(RobotStateContext context) {
        IRobot robot = context.getRobot();
        ColorType colorType = robot.getColor();
        ConsoleHelper.printlnPurple("Line color to follow: " + colorType.name());
        context.setState(new FollowLineState(colorType));
    }
}
