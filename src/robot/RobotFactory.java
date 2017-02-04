package robot;

import config.RobotConfig;

public class RobotFactory {
	public static IRobot CreateRobot() {
		if(RobotConfig.UseRobotEv3) {
			return new EV3Tank();
		} else if (RobotConfig.UseRobotMockup) {
			return new MockupRobot();
		} else if (RobotConfig.UseRobotReal) {
			return new Tank();
		} else {
			return new MockupRobot();
		}
	}
}
