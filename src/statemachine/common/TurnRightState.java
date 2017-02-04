package statemachine.common;

import config.RobotConfig;
import robot.IRobot;
import robot.RobotMessage;
import statemachine.RobotState;
import statemachine.RobotStateContext;
import util.ConsoleHelper;

public class TurnRightState extends RobotState {

    private boolean turnRightToTargetAngle = false;
    private int targetDegrees = -1;
    private int offset = -1;
    private int lastAngle = -1;
    private int startAngle = -1;
    private boolean beyondZero = false;

    public TurnRightState(int targetDegrees, int offset) {
        this.targetDegrees = targetDegrees;
        this.offset = Math.abs(offset);
    }

    @Override
    public String getName() {
        return "Turn right";
    }

    @Override
    public void initState(RobotStateContext context) {
        this.startAngle = context.getRobot().getRoundedAngle();
        if (this.startAngle + this.offset > 360) {
            ConsoleHelper.printlnDefault("BeyondZero = true");
            this.beyondZero = true;
        }

        this.turnRightToTargetAngle = true;
        context.getRobot().setSpeed(RobotConfig.TurnMotorSpeed);
        context.getRobot().startTurnRight();
    }

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        switch (message) {
            case AngleChanged: {
                if (!turnRightToTargetAngle) return;

                int roundedAngle = (int)Float.parseFloat(data);
                if (this.lastAngle == -1) this.lastAngle = roundedAngle;

                int tempLastAngle = this.lastAngle;
                this.lastAngle = roundedAngle;

                if (roundedAngle < tempLastAngle &&
                        !(lastAngle <= 360 && ((roundedAngle < this.startAngle) && (roundedAngle >= 0)))) { // handle north pole
                    ConsoleHelper.printlnYellow("Terminated left movement. New Angle: " + roundedAngle + " / Last Angle: " + tempLastAngle);
                    return; // Terminate left rotations
                }

                if (this.beyondZero) {
                    if (roundedAngle <= 360 && roundedAngle >= this.startAngle) {
                        // stop execution until the north point has been reached
                        return;
                    } else {
                        ConsoleHelper.printlnDefault("BeyondZero = false");
                        this.beyondZero = false;
                    }
                }

                if(roundedAngle >= this.targetDegrees) {
                    this.turnRightToTargetAngle = false;
                    IRobot robot = context.getRobot();
                    robot.stopTurnRight();
                    robot.setSpeed(RobotConfig.DefaultMotorSpeed);
                }

                break;
            }
            case TurnRightStopped: {
                context.setState(new OnlineState());
                break;
            }
        }
    }
}
