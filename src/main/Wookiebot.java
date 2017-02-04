package main;

import robot.IRobot;
import robot.RobotFactory;
import shared.UserCommand;
import shared.UserCommandContainer;
import statemachine.RobotState;
import statemachine.RobotStateContext;
import util.RobotRunner;

public class Wookiebot {
    private IRobot robot;
    private util.KeyListener keyListener;
    private RobotRunner robotRunner;

    private RobotStateContext robotContext;
    public RobotStateContext getRobotStateContext() {
        return this.robotContext;
    }

    private Thread tRobot = null;
    private Thread tKey = null;

    public Wookiebot() {
        this.robot = RobotFactory.CreateRobot();
        this.robotContext = new RobotStateContext(robot);
        this.robotRunner = new RobotRunner(this.robotContext);
        this.keyListener = new util.KeyListener(this.robotRunner);

        this.tRobot = new Thread(this.robotRunner);
        this.tKey = new Thread(this.keyListener);
    }

    public void start() {
        this.tRobot.start();
        this.tKey.start();
    }

    public void stop() {
        this.keyListener.abort();
        this.robotRunner.stop();
    }

    public void turnLeft(int degrees) {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.intArg = degrees;
        cmd.userCommand = UserCommand.LEFT;
        this.robotRunner.forwardCommand(cmd);
    }

    public void turnRight(int degrees) {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.intArg = degrees;
        cmd.userCommand = UserCommand.RIGHT;
        this.robotRunner.forwardCommand(cmd);
    }

    public void followLine() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.LINE;
        this.robotRunner.forwardCommand(cmd);
    }

    public void startListenDistance() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.START_LISTEN_DISTANCE;
        this.robotRunner.forwardCommand(cmd);
    }

    public void stopListenDistane() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.STOP_LISTEN_DISTANCE;
        this.robotRunner.forwardCommand(cmd);
    }

    public void startListenColor() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.START_LISTEN_COLOR;
        this.robotRunner.forwardCommand(cmd);
    }

    public void stopListenColor() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.STOP_LISTEN_COLOR;
        this.robotRunner.forwardCommand(cmd);
    }

    public void startListenCompass() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.START_LISTEN_COMPASS;
        this.robotRunner.forwardCommand(cmd);
    }

    public void stopListenCompass() {
        UserCommandContainer cmd = new UserCommandContainer();
        cmd.userCommand = UserCommand.STOP_LISTEN_COMPASS;
        this.robotRunner.forwardCommand(cmd);
    }
}
