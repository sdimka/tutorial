package org.test.getData;

import java.time.LocalDateTime;

public class backGroundTask implements Runnable{
    LocalDateTime time;
    double temp;
    double hum;
    double press;
    DataProvider dataProvider;

    @Override
    public void run() {
        dataProvider = DataProvider.getInst();
//        GetSensData gsd = GetSensData.getInst();

        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time = LocalDateTime.now();
            temp = Math.random() * 10;
            hum = Math.random() * 100;
            press = Math.random() * 750;

//            temp = gsd.getTemp();
//            hum = gsd.getHum();
//            press = gsd.getPressure();

            dataProvider.updateInfo(time,temp,hum,press);
        }
    }
}
