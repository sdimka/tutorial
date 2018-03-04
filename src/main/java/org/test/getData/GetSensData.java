package org.test.getData;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

import java.io.IOException;

public class GetSensData {
    private static GetSensData instance = new GetSensData();
    //private static final instance = new
    // BME280 I2C address
    private static final int BME280_ADDR = 0x76; // address pin not connected (FLOATING)
    //public static final int TSL2561_ADDR = 0x29; // address pin connect to GND
    //public static final int TSL2561_ADDR = 0x49; // address pin connected to VDD

    // BME280 registers
    private static final byte BME280_REG_ID = (byte)0xD0;
    private static final byte BME280_REG_TEMP = (byte)0xFA;
    private static final byte BME280_REG_PRESS = (byte)0xF7;
    private static final byte BME280_REG_HUMID = (byte)0xFD;
    private static final byte BME280_REG_CONFIG = (byte)0xF5;

    private static final byte BME280_DIG_T1 = (byte)0x88;
    private static final byte BME280_DIG_T2 = (byte)0x8A;
    private static final byte BME280_DIG_T3 = (byte)0x8C;
    public static final byte BME280_DIG_P1 = (byte)0x8E;
    public static final byte BME280_DIG_P2 = (byte)0x90;
    public static final byte BME280_DIG_P3 = (byte)0x92;
    public static final byte BME280_DIG_P4 = (byte)0x94;
    public static final byte BME280_DIG_P5 = (byte)0x96;
    public static final byte BME280_DIG_P6 = (byte)0x98;
    public static final byte BME280_DIG_P7 = (byte)0x9A;
    public static final byte BME280_DIG_P8 = (byte)0x9C;
    public static final byte BME280_DIG_P9 = (byte)0x9E;
    public static final byte BME280_DIG_H1 = (byte)0xA1;
    public static final byte BME280_DIG_H2 = (byte)0xE1;
    public static final byte BME280_DIG_H3 = (byte)0xE3;
    public static final byte BME280_DIG_H4 = (byte)0xE4;
    public static final byte BME280_DIG_H5 = (byte)0xE5;
    public static final byte BME280_DIG_H6 = (byte)0xE7;
    // TSL2561 power control values
    public static final byte TSL2561_POWER_UP = (byte)0x03;
    public static final byte TSL2561_POWER_DOWN = (byte)0x00;

    public static int nDevices;

    I2CBus i2c;
    I2CDevice device;

    private GetSensData(){
        try {
            PlatformManager.setPlatform(Platform.ODROID);
            i2c = I2CFactory.getInstance(I2CBus.BUS_1);
            device = i2c.getDevice(BME280_ADDR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GetSensData getInst() {
        return instance;
    }

    public double getTemp(){
        double temperature = (getTFine() / 5120.0);
        return temperature;
    }

    public double getPressure(){
        double pressure = 0;
        int dig_p1 = 0, dig_p2 = 0, dig_p3 = 0, dig_p4 = 0,
        dig_p5 = 0, dig_p6 = 0, dig_p7 = 0, dig_p8 = 0,
        dig_p9 = 0, adc_p = 0;
        try {
            dig_p1 = read_word(BME280_DIG_P1);      // Unsigned
            dig_p2 = read_word_sign(BME280_DIG_P2);
            dig_p3 = read_word_sign(BME280_DIG_P3);
            dig_p4 = read_word_sign(BME280_DIG_P4);
            dig_p5 = read_word_sign(BME280_DIG_P5);
            dig_p6 = read_word_sign(BME280_DIG_P6);
            dig_p7 = read_word_sign(BME280_DIG_P7);
            dig_p8 = read_word_sign(BME280_DIG_P8);
            dig_p9 = read_word_sign(BME280_DIG_P9);
            adc_p = read_adc_long(BME280_REG_PRESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double var1 = (getTFine()/2.0) - 64000.0;
        double var2 = var1 * var1 * dig_p6 / 32768.0;
        var2 = var2 + var1 * dig_p5 * 2.0;
        var2 = (var2/4.0)+(dig_p4 * 65536.0);
        var1 = (dig_p3 * var1 * var1 / 524288.0 + dig_p2 * var1) / 524288.0;
        var1 = (1.0 + var1 / 32768.0) * dig_p1;
        if (var1 != 0.0) {
            double p = 1048576.0 - adc_p;
            p = (p - (var2 / 4096.0)) * 6250.0 / var1;
            var1 = dig_p9 * p * p / 2147483648.0;
            var2 = p * dig_p8 / 32768.0;
            pressure = ((p + (var1 + var2 + dig_p7) / 16.0));
        }
        return pressure/133;
    }

    public double getHum(){
        double var_H = getTFine() - 76800.0;

        int dig_h1 = 0;	// unsigned char
        int dig_h2 = 0;
        int dig_h3 = 0;	// unsigned char
        int dig_h4 = 0;
        int dig_h5 = 0;
        int adc_h = 0;
        int dig_h6 = 0;
        try {
            dig_h1 = read_byte(BME280_DIG_H1);
            dig_h2 = read_word_sign(BME280_DIG_H2);
            dig_h3 = read_byte(BME280_DIG_H3);
            dig_h4 = (read_byte(BME280_DIG_H4) << 24) >> 20;
            dig_h4 = dig_h4 | read_byte((byte)(BME280_DIG_H4 + 1)) & 0x0F;
            dig_h5 = (read_byte((byte)(BME280_DIG_H5 + 1)) << 24) >> 20;
            dig_h5 = dig_h5 | (read_byte(BME280_DIG_H5) >> 4) & 0x0F;
            dig_h6 = read_byte(BME280_DIG_H6);
            adc_h = read_adc_word(BME280_REG_HUMID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        var_H = (adc_h-(dig_h4*64.0 + dig_h5 / 16384.0 * var_H))*
                (dig_h2/65536.0 * (1.0 + dig_h6/67108864.0 * var_H * (1.0 + dig_h3/67108864.0 * var_H)));
        double humidity = Math.round(var_H * (1.0 - dig_h1 * var_H / 524288.0));
        if (humidity > 100.0)
            humidity = 100.0;
        else if(humidity < 0.0) humidity = 0.0;
        return humidity;
    }

    private double getTFine(){
        int dig_t1 = 0, dig_t2 = 0, dig_t3 = 0, adc_t = 0;
        try {
            dig_t1 = read_word(BME280_DIG_T1);      // Unsigned
            dig_t2 = read_word_sign(BME280_DIG_T2);
            dig_t3 = read_word_sign(BME280_DIG_T3);
            adc_t = read_adc_long(BME280_REG_TEMP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double var1 = (adc_t/16384.0 - dig_t1/1024.0) * dig_t2;
        double var2 = ((adc_t/131072.0 - dig_t1/8192.0) * (adc_t/131072.0 - dig_t1/8192.0)) * dig_t3;
        double t_fine = (var1 + var2);
        return t_fine;
    }

    public String getStatus(){
        int response = 0;
        try {
            response = device.read(BME280_REG_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "TSL2561 ID = " + response;
    }

    private int read_byte(byte rb)throws InterruptedException, PlatformAlreadyAssignedException, IOException, UnsupportedBusNumberException{
        return device.read(rb);
    }

    private int read_word(byte rb)throws InterruptedException, PlatformAlreadyAssignedException, IOException, UnsupportedBusNumberException{
        // ATTANTION! Joke from Bosch! LBS before HBS. For calibration registers only!
        int lbs = device.read(rb);
        int hbs = device.read(rb+1);
        return (hbs << 8) + lbs;
    }

    private int read_word_sign(byte rb)throws InterruptedException, PlatformAlreadyAssignedException, IOException, UnsupportedBusNumberException {
        int val = read_word(rb);
        if (val >= 0x8000)
            return -((65535 - val) + 1);
        else
            return val;
    }

    private int read_adc_long(byte rb)throws InterruptedException, PlatformAlreadyAssignedException, IOException, UnsupportedBusNumberException{
        int mbs = device.read(rb);
        int lbs = device.read(rb+1);
        int xbs = device.read(rb+2);
        int val = (mbs << 16) + (lbs << 8) + xbs;
        val >>= 4;
        return val;
    }

    private int read_adc_word(byte rb)throws InterruptedException, PlatformAlreadyAssignedException, IOException, UnsupportedBusNumberException{
        int mbs = device.read(rb);
        int lbs = device.read(rb+1);
        int val = (mbs << 8) + lbs;
        return val;
    }
}
