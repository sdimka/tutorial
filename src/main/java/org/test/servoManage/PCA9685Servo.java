package org.test.servoManage;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

import java.io.IOException;
import java.math.BigDecimal;

public class PCA9685Servo {

    private static final int SERVO_DURATION_MIN = 900;
    private static final int SERVO_DURATION_NEUTRAL = 1500;
    private static final int SERVO_DURATION_MAX = 2100;
    I2CBus bus;
    PCA9685GpioProvider provider;
    GpioPinPwmOutput[] myOutputs;

    public PCA9685Servo() throws PlatformAlreadyAssignedException, IOException, I2CFactory.UnsupportedBusNumberException {
        BigDecimal frequency = new BigDecimal("48.828");
        // Correction factor: actualFreq / targetFreq
        // e.g. measured actual frequency is: 51.69 Hz
        // Calculate correction factor: 51.65 / 48.828 = 1.0578
        // --> To measure actual frequency set frequency without correction factor(or set to 1)
        BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");
        PlatformManager.setPlatform(Platform.ODROID);
        bus = I2CFactory.getInstance(I2CBus.BUS_2);
        provider = new PCA9685GpioProvider(bus, 0x40, frequency, frequencyCorrectionFactor);
        // Define outputs in use for this example
        myOutputs = provisionPwmOutputs(provider);
        // Reset outputs
        provider.reset();
    }

    private GpioPinPwmOutput[] provisionPwmOutputs(final PCA9685GpioProvider gpioProvider) {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinPwmOutput myOutputs[] = {
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Servo 00"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Servo 01"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Servo 02"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "none"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "none"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "none"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "none"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "none"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "none"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "Pulse 09"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "Always ON"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "Always OFF"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "Servo pulse MIN"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "Servo pulse NEUTRAL"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "Servo pulse MAX"),
                gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "not used")};
        return myOutputs;
    }

    public static void main(String[] args) {

        PCA9685Servo pca9685Servo;
        try {
            pca9685Servo = new PCA9685Servo();
        } catch (PlatformAlreadyAssignedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
        }
    }
}
