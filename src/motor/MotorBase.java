package motor;

import util.ConsoleHelper;

import java.util.ArrayList;
import java.util.List;

public class MotorBase implements IMotor, IMotorObservable {
    protected int minSpeedAbs = 0;
    protected int maxSpeedAbs = 100;

    protected int currentSpeed = 0;

    protected  MotorType motorType = MotorType.UNDEFINED;

    private boolean isRotating = false;

    //region Observer Pattern
    private boolean finishUnsubscribeCalled = false;
    protected List<IMotorObserver> observers = new ArrayList<IMotorObserver>();
    protected List<IMotorObserver> observersToRemove = new ArrayList<IMotorObserver>();
    //endregion

    public MotorBase(int minSpeedAbs, int maxSpeedAbs, MotorType motorType) {
        this.maxSpeedAbs = maxSpeedAbs;
        this.minSpeedAbs = minSpeedAbs;
        this.motorType = motorType;
    }

    @Override
    public void setSpeed(int speedInPercent) {
        if (speedInPercent == 0) {
            // 0 = Stop
            this.currentSpeed = 0;
        } else if (speedInPercent == 1) {
            // 1 = Min Speed
            this.currentSpeed = this.minSpeedAbs;
        } else if (speedInPercent == 100) {
            // 100 = Max Speed
            this.currentSpeed = this.maxSpeedAbs;
        } else {
            // 2..100 Calculate Speed
            int diff = Math.abs(this.maxSpeedAbs - this.minSpeedAbs);
            float onePercent = (float)diff / (float)100;
            int offset = this.minSpeedAbs;
            if (this.minSpeedAbs > this.maxSpeedAbs) {
                offset *= -1;
            }
            float temp = Math.abs((onePercent * speedInPercent) + offset);
            this.currentSpeed = Math.round(temp);
        }
        ConsoleHelper.printlnDefault("Motor (" + this.motorType.name() + "): Speed is set to " + this.currentSpeed);
    }

    @Override
    public int getSpeed() {
        return this.currentSpeed;
    }

    @Override
    public void forward() {
        this.setIsRotating(true);
    }

    @Override
    public void backward() {
        this.setIsRotating(true);
    }

    @Override
    public void stop() {
        this.setIsRotating(false);
    }

    @Override
    public void init() {

    }

    @Override
    public void deInit() {

    }

    @Override
    public boolean isRotating() {
        return this.isRotating;
    }

    @Override
    public void setIsRotating(boolean isRotating) {
        boolean oldValue = this.isRotating;
        boolean newValue = isRotating;

        this.isRotating = isRotating;

        // Check if the value has changed
        if(oldValue != newValue) {
            if (newValue) this.notifyMotorObservers(MotorMessage.MotorStarted, this.motorType);
            else this.notifyMotorObservers(MotorMessage.MotorStopped, this.motorType);
        }
    }

    //region Implement IMotorObervable

    @Override
    public void subscribeToMotorMessages(IMotorObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void unsubscribeToMotorMessages(IMotorObserver observer) {
        this.observersToRemove.add(observer);
    }

    @Override
    public void notifyMotorObservers(MotorMessage message, MotorType motorType) {
        try {
            for (IMotorObserver observer : this.observers) observer.handleMotorMessage(message, motorType);
        } catch (java.util.ConcurrentModificationException concurrentModificationException) {
            System.out.println(concurrentModificationException);
        }

        if(this.finishUnsubscribeCalled) {
            this.observers.removeAll(this.observersToRemove);
            this.observersToRemove.clear();

            this.finishUnsubscribeCalled = false;
        }
    }

    @Override
    public void finishUnsubscribeToMotorMessages() {
        this.finishUnsubscribeCalled = true;
    }

    //endregion
}
