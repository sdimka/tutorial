package org.test.servoManage;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.impl.PinImpl;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;

public class PCA9685Servo {

    private static PCA9685Servo instance = new PCA9685Servo();

    private boolean isReal;
    private static final int SERVO_DURATION_MIN = 900;
    private static final int SERVO_DURATION_NEUTRAL = 1500;
    private static final int SERVO_DURATION_MAX = 2100;
    private I2CBus bus;
    private PCA9685GpioProvider provider;
    private GpioPinPwmOutput[] myOutputs = new GpioPinPwmOutput[15];

    private static ArrayList<Pin> pins = new ArrayList<>();
    static {
        pins.add(createPwmPin(0, "PWM 0"));
        pins.add(createPwmPin(1, "PWM 0"));
        pins.add(createPwmPin(2, "PWM 0"));
    }

    private static Pin createPwmPin(int channel, String name) {
        return new PinImpl("com.pi4j.gpio.extension.pca.PCA9685GpioProvider", channel, name, EnumSet.of(PinMode.PWM_OUTPUT));
    }

    public static PCA9685Servo getInstance(){
        return instance;
    }

    private PCA9685Servo(){

        isReal = true;

        BigDecimal frequency = new BigDecimal("48.828");
        // Correction factor: actualFreq / targetFreq
        // e.g. measured actual frequency is: 51.69 Hz
        // Calculate correction factor: 51.65 / 48.828 = 1.0578
        // --> To measure actual frequency set frequency without correction factor(or set to 1)
        BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");

        try {
            PlatformManager.setPlatform(Platform.ODROID);
            bus = I2CFactory.getInstance(I2CBus.BUS_2);
            provider = new PCA9685GpioProvider(bus, 0x40, frequency, frequencyCorrectionFactor);
        } catch (PlatformAlreadyAssignedException e) {
            e.printStackTrace();
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
            isReal = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Define outputs in use for this example
        if (isReal) {
            GpioController gpio = GpioFactory.getInstance();
            for (int i = 0; i < pins.size(); i++) {
                myOutputs[i] = gpio.provisionPwmOutputPin(provider, pins.get(i), "Servo" + i);
            }
            // Reset outputs
            provider.reset();
        }
    }

    public boolean isReal() {
        return isReal;
    }

    public int GetPosition (int pinNumber){ // Return position in percent from neutral
        if (isReal) {
            if (checkPinExist(pinNumber))
                return (provider.getPwm(pins.get(pinNumber)) - 1500) / 6;
            else return -1;
        } else return 50;
    }

    private boolean checkPinExist(int pinNumber){
        if (pinNumber < pins.size() - 1 )
            return true;
        else return false;
    }

    public void Move(int pinNubmer, int newPosition){ // ToDo Change position to percent
        // 0% - 1500 100% - 2100, 1% = 6
        if (checkPinExist(pinNubmer)){
            if (GetPosition(pinNubmer) != newPosition){
                Mover mover = new Mover(pinNubmer, newPosition);
                mover.start();
            }
        }
    }


    private class Mover extends Thread{
        private int speed = 3;
        private final int step = 6;
        private final int maxSleepBetweenSteps = 100;
        private int newPosition; // Position in wave lenight
        private Pin pin;

        public Mover(int pinNumber, int newPosition) {
            this.pin = pins.get(pinNumber);
            this.newPosition = (newPosition * 6) + 1500;
        }

        @Override
        public void run() {
            int currentPosition = provider.getPwm(pin);
            while (currentPosition < newPosition) {
                currentPosition += step;
                provider.setPwm(pin, currentPosition);
                try {
                    Thread.sleep(maxSleepBetweenSteps/speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
