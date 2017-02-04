package motor;

import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

import java.rmi.RemoteException;

public class EV3Motor extends MotorBase {

    private RMIRegulatedMotor motor;
    private boolean brakeOnStop;

    public EV3Motor(RemoteEV3 ev3, Character motorType, String motorPort, boolean brakeOnStop, MotorType motorType2) {
        super(150, 740, motorType2);

        this.brakeOnStop = brakeOnStop;

        this.motor = ev3.createRegulatedMotor(motorPort, motorType);
    }

    @Override
    public void setSpeed(int speedInPercentage) {
        super.setSpeed(speedInPercentage);

        try {
            this.motor.setSpeed(super.currentSpeed);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void forward() {
        super.forward();

        try {
            this.motor.forward();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void backward() {
        super.backward();

        try {
            this.motor.backward();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        super.stop();

        try {
            this.motor.stop(this.brakeOnStop);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deInit() {
        super.deInit();

        try {
            this.motor.close();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
