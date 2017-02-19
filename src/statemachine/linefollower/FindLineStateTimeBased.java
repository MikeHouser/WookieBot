package statemachine.linefollower;

import config.RobotConfig;
import robot.IRobot;
import robot.RobotMessage;
import shared.ColorType;
import statemachine.RobotState;
import statemachine.RobotStateContext;
import util.ConsoleHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Time based algorithm that can be used when no angle detection is possible
 */
public class FindLineStateTimeBased extends RobotState {

    class TimeoutTask extends TimerTask {
        private RobotStateContext context;
        private ColorType colorType;
        private int timeoutMs;
        private boolean turnLeft;

        public boolean Cancel = false;
        public boolean Activated = false;

        public TimeoutTask(int timeoutMs, RobotStateContext context, ColorType colorType, boolean turnLeft) {
            this.timeoutMs = timeoutMs;
            this.colorType = colorType;
            this.context = context;
            this.turnLeft = turnLeft;
        }

        @Override
        public void run() {
            // Semaphore
            if (this.Cancel) return;
            this.Activated = true;

            ConsoleHelper.printlnPurple("Timout reached in 'FindLineStateTimeBased'.");

            this.timeoutMs += this.timeoutMs;

            if (this.turnLeft) this.context.getRobot().stopTurnLeft();
            else this.context.getRobot().stopTurnRight();

            // New state
            this.context.setState(new FindLineStateTimeBased(this.timeoutMs, this.colorType, !this.turnLeft));
        }
    }

    private Timer timer;
    private TimeoutTask action;
    private int timeoutMs;
    private ColorType colorType;
    private boolean turnLeft = true;

    public FindLineStateTimeBased(int timeoutMs, ColorType colorType, boolean turnLeft) {
        this.timeoutMs = timeoutMs;
        this.colorType = colorType;
        this.turnLeft = turnLeft;
    }

    @Override
    public String getName() {
        return "Find line";
    }

    @Override
    public void initState(RobotStateContext context) {
        super.initState(context);

        IRobot robot = context.getRobot();

        // Check if transition can be done
        if(robot.getColor() == this.colorType) {
            this.Transition(context);
            return;
        }

        // Prepare timeout
        this.action = new TimeoutTask(this.timeoutMs, context, this.colorType, this.turnLeft);
        this.timer = new Timer("FindLineStateTimeBased-Timer", true);
        this.timer.schedule(action, this.timeoutMs);

        // Start action
        robot.setSpeed(RobotConfig.getSearchMotorSpeedInPercent());
        if (this.turnLeft) robot.startTurnLeft();
        else robot.startTurnRight();
    }

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        super.handleRobotMessage(context, message, data);

        switch (message) {
            case ColorChanged: {
                ColorType newColorType = ColorType.valueOf(data);
                if(newColorType == this.colorType) {
                    // Semaphore
                    if (this.action.Activated) return;
                    this.action.Cancel = true;

                    // Stop timer
                    this.timer.cancel();
                    this.timer.purge();

                    this.Transition(context);
                }
            }
        }
    }

    @Override
    public void stopRobot(RobotStateContext context) {
        // Stop timer
        this.timer.cancel();
        this.timer.purge();

        super.stopRobot(context);
    }

    private void Transition(RobotStateContext context) {
        IRobot robot = context.getRobot();

        if (this.turnLeft) robot.stopTurnLeft();
        else robot.stopTurnRight();
        robot.setSpeed(RobotConfig.getDefaultMotorSpeedInPercent());

        robot.Beep();

        // New state
        context.setState(new FollowLineState(this.colorType));
    }
}

