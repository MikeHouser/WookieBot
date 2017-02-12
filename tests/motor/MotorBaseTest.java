package motor;

import org.junit.Test;

import static org.junit.Assert.*;

public class MotorBaseTest {
    @Test
    public void setSpeed() throws Exception {
        int minSpeed = 100;
        int maxSpeed = 3;

        MotorBase motorBase = new MotorBase(minSpeed, maxSpeed, MotorType.UNDEFINED);

        motorBase.setSpeed(0);
        assertEquals(motorBase.currentSpeed, 0);

        // min Speed -> 100ms sleep insted of 3ms sleep
        motorBase.setSpeed(1);
        assertEquals(motorBase.currentSpeed, minSpeed);

        // max Speed -> 3ms sleep instead of 100ms sleep
        motorBase.setSpeed(100);
        assertEquals(motorBase.currentSpeed, maxSpeed);

        // diff = 100 - 3 = 97
        // 1% = 97 / 100 = 0,97
        motorBase.setSpeed(30);
        assertEquals(motorBase.currentSpeed, 71);

        motorBase.setSpeed(70);
        assertEquals(motorBase.currentSpeed, 32);

        minSpeed = 0;
        maxSpeed = 100;

        motorBase = new MotorBase(minSpeed, maxSpeed, MotorType.UNDEFINED);

        motorBase.setSpeed(10);
        assertEquals(motorBase.currentSpeed, 10);

        motorBase.setSpeed(50);
        assertEquals(motorBase.currentSpeed, 50);

        motorBase.setSpeed(80);
        assertEquals(motorBase.currentSpeed, 80);
    }

}