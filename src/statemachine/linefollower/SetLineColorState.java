package statemachine.linefollower;

import robot.IRobot;
import shared.ColorType;
import statemachine.RobotStateContext;

import java.text.MessageFormat;

public class SetLineColorState extends LineFollowerState {

    @Override
    public void initState(RobotStateContext context) {
        IRobot robot = context.getRobot();
        ColorType colorType = robot.getColor();
        super.log(MessageFormat.format("Line color to follow: {0}", colorType.name()));
        context.setState(new FollowLineState(colorType));
    }
}
