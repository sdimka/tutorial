package org.test.getData;

import java.time.LocalDateTime;

public class backGroundTask implements Runnable{
    private LocalDateTime time;
    private double temp;
    private double temp2;
    private double hum;
    private double press;
    private DataProvider dataProvider;
    private GetSensData gsd;
    private DS18B20Read ds18B20Read;


    @Override
    public void run() {
        dataProvider = DataProvider.getInst();
        ds18B20Read = DS18B20Read.getInstance();

        while (true) {

            if (dataProvider.isSensorUse()){
                gsd = GetSensData.getInst();
                time = LocalDateTime.now();
                temp = gsd.getTemp();
                temp2 = ds18B20Read.getTemp();
                hum = gsd.getHum();
                press = gsd.getPressure();
            } else {
                time = LocalDateTime.now();
                temp = Math.random() * 10;
                temp2 = Math.random() * 10;
                hum = Math.random() * 100;
                press = Math.random() * 750;
            }

            dataProvider.updateInfo(time, temp, temp2, hum, press);

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
