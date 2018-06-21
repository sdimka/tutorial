package org.test.servoManage;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.*;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

import java.io.IOException;
import java.math.BigDecimal;

public class PCA9685Servo {

    private static PCA9685Servo instance = new PCA9685Servo();

    private boolean isReal;
    private static final int SERVO_DURATION_MIN = 900;
    private static final int SERVO_DURATION_NEUTRAL = 1500;
    private static final int SERVO_DURATION_MAX = 2100;
    private I2CBus bus;
    private PCA9685GpioProvider provider;
    private GpioPinPwmOutput[] myOutputs;
    private ServoCoordinates servoCoordinates;

    public static PCA9685Servo getInstance(){
        return instance;
    }

    private PCA9685Servo(){

        isReal = true;
        servoCoordinates = ServoCoordinates.getInstance();

        BigDecimal frequency = new BigDecimal("48.828");
        // Correction factor: actualFreq / targetFreq
        // e.g. measured actual frequency is: 51.69 Hz
        // Calculate correction factor: 51.65 / 48.828 = 1.0578
        // --> To measure actual frequency set frequency without correction factor(or set to 1)
        BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");

        try {
            //PlatformManager.setPlatform(Platform.ODROID);
            bus = I2CFactory.getInstance(I2CBus.BUS_2);
            provider = new PCA9685GpioProvider(bus, 0x40, frequency, frequencyCorrectionFactor);

        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
            isReal = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Define outputs in use for this example
        if (isReal) {
            GpioController gpio = GpioFactory.getInstance();
            myOutputs = provisionPwmOutputs(provider);
            // Reset outputs
            provider.reset();
        }
    }

    public boolean isReal() {
        return isReal;
    }

    public int GetPosition (int pinNumber){ // Return position in percent from neutral
        return (servoCoordinates.getCoord(pinNumber) - 1500) / 6;

    }

//    public int[] GetPwm (int pinNumber){
//        return provider.getPwmOnOffValues(myOutputs[pinNumber].getPin());
//    }

    public void Move(int pinNubmer, int newPosition){ // Change position in percent
        // 0% - 1500 100% - 2100, 1% = 6
            if (GetPosition(pinNubmer) != newPosition){
                Mover mover = new Mover(pinNubmer, newPosition);
                mover.start();
            }
    }


    private class Mover extends Thread{
        private int speed = 3;
        private final int step = 6;
        private final int maxSleepBetweenSteps = 100;
        private int newPosition; // Position in wave lenight
        private int pinNumber;

        public Mover(int pinNumber, int newPosition) {
            this.pinNumber = pinNumber;
            this.newPosition = (newPosition * 6) + 1500;
        }

        @Override
        public void run() {
            int currentPosition = servoCoordinates.getCoord(pinNumber);
            if (currentPosition < newPosition) {
                while (currentPosition <= newPosition) {
                    currentPosition += step;
                    if (isReal) {
                        provider.setPwm(myOutputs[pinNumber].getPin(), currentPosition);
                    }
                    try {
                        Thread.sleep(maxSleepBetweenSteps / speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (currentPosition > newPosition) {
                while (currentPosition >= newPosition) {
                    currentPosition -= step;
                    if (isReal) {
                        provider.setPwm(myOutputs[pinNumber].getPin(), currentPosition);
                    }
                    try {
                        Thread.sleep(maxSleepBetweenSteps / speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            servoCoordinates.setCoord(pinNumber, currentPosition);
        }
    }

    private static GpioPinPwmOutput[] provisionPwmOutputs(final PCA9685GpioProvider gpioProvider) {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinPwmOutput myOutputs[] = {
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Servo 00"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Servo 01"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Servo 02"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Pulse 03"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "Pulse 04"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Pulse 05"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "Pulse 06"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "Pulse 07"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "Pulse 08"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "Pulse 09"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "Always ON"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "Always OFF"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "Servo pulse MIN"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "Servo pulse NEUTRAL"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "Servo pulse MAX"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "not used")};
        return myOutputs;
    }
}
