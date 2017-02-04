package util;

import robot.*;
import shared.UserCommand;
import shared.UserCommandContainer;
import statemachine.RobotStateContext;

public class RobotRunner implements Runnable {

    private RobotStateContext robotContext;

    public RobotRunner(RobotStateContext robotContext) {
        this.robotContext = robotContext;
    }

    public void run() {
        this.robotContext.startRobot();
    }

    public void stop() {
        this.robotContext.stopRobot();
    }

    public void forwardCommand(UserCommandContainer command) {
        this.robotContext.forwardCommand(command);
    }
}
