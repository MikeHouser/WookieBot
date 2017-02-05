package robot;

import config.RobotConfig;

public class RobotFactory {
    public static IRobot CreateRobot() {
        if(RobotConfig.getUseLegoRobot()) {
            return new EV3Tank();
        } else if (RobotConfig.getUseMockupRobot()) {
            return new MockupRobot();
        } else if (RobotConfig.getUseMakerRobot()) {
            return new Tank();
        } else {
            return new MockupRobot();
        }
    }
}
