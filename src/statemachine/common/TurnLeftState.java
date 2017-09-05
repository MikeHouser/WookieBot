package statemachine.common;

import config.RobotConfig;
import robot.IRobot;
import robot.RobotMessage;
import statemachine.RobotState;
import statemachine.RobotStateContext;

public class TurnLeftState extends RobotState {

    private boolean turnLeftToTargetAngle = false;
    private int targetDegrees = -1;
    private int offset = -1;
    private int lastAngle = -1;
    private int startAngle = -1;
    private boolean beyondZero = false;

    public TurnLeftState(int targetDegrees, int offset) {
        this.targetDegrees = targetDegrees;
        this.offset = Math.abs(offset);
    }

    @Override
    public void initState(RobotStateContext context) {
        this.startAngle = context.getRobot().getRoundedAngle();
        if (this.startAngle - this.offset < 0) {
            super.log("BeyondZero = true");
            this.beyondZero = true;
        }

        this.turnLeftToTargetAngle = true;
        context.getRobot().setSpeed(RobotConfig.getTurnMotorSpeedInPercent());
        context.getRobot().startTurnLeft();
    }

    @Override
    public void handleRobotMessage(RobotStateContext context, RobotMessage message, String data) {
        switch (message) {
            case AngleChanged: {
                if (!this.turnLeftToTargetAngle) return;

                int roundedAngle = (int)Float.parseFloat(data);
                if (this.lastAngle == -1) this.lastAngle = roundedAngle;

                int tempLastAngle = this.lastAngle;
                this.lastAngle = roundedAngle;

                if (roundedAngle > tempLastAngle &&
                    !(lastAngle >= 0 && ((roundedAngle > this.startAngle) && (roundedAngle <= 360)))) { // handle north pole
                    super.log("Terminated right movement. New Angle: " + roundedAngle + " / Last Angle: " + tempLastAngle);
                    return; // Terminate right rotations
                }

                if (this.beyondZero) {
                    if (roundedAngle >= 0 && roundedAngle <= this.startAngle) {
                        // stop execution until the north point has been reached
                        return;
                    } else {
                        super.log("BeyondZero = false");
                        this.beyondZero = false;
                    }
                }

                if(roundedAngle <= this.targetDegrees) {
                    this.turnLeftToTargetAngle = false;
                    IRobot robot = context.getRobot();
                    robot.stopTurnLeft();
                    robot.setSpeed(RobotConfig.getDefaultMotorSpeedInPercent());
                }

                break;
            }
            case TurnLeftStopped: {
                context.setState(new OnlineState());
                break;
            }
        }
    }
}
