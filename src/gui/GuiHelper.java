package gui;

import shared.UserCommand;
import shared.UserCommandContainer;
import statemachine.RobotStateContext;

public class GuiHelper {
    private RobotStateContext robotContext;

    public GuiHelper(RobotStateContext robotStateContext) {
        this.robotContext = robotStateContext;
    }

    public void startRobot() {
        this.robotContext.startRobot();
    }

    public void stopRobot() {
        this.robotContext.stopRobot();
    }

    public void turnLeft(int degrees) {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.intArg = degrees;
        cmd.userCommand = UserCommand.LEFT;
        this.robotContext.forwardCommand(cmd);
    }

    public void turnRight(int degrees) {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.intArg = degrees;
        cmd.userCommand = UserCommand.RIGHT;
        this.robotContext.forwardCommand(cmd);
    }

    public void startFollowLine() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.START_LINE;
        this.robotContext.forwardCommand(cmd);
    }

    public void stopFollowLine() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.STOP_LINE;
        this.robotContext.forwardCommand(cmd);
    }

    public void startListenDistance() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.START_LISTEN_DISTANCE;
        this.robotContext.forwardCommand(cmd);
    }

    public void stopListenDistane() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.STOP_LISTEN_DISTANCE;
        this.robotContext.forwardCommand(cmd);
    }

    public void startListenColor() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.START_LISTEN_COLOR;
        this.robotContext.forwardCommand(cmd);
    }

    public void stopListenColor() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.STOP_LISTEN_COLOR;
        this.robotContext.forwardCommand(cmd);
    }

    public void startListenCompass() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.START_LISTEN_COMPASS;
        this.robotContext.forwardCommand(cmd);
    }

    public void stopListenCompass() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.STOP_LISTEN_COMPASS;
        this.robotContext.forwardCommand(cmd);
    }
}
