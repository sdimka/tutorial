package org.test.getData;

import java.time.LocalDateTime;

public class backGroundTask implements Runnable{
    LocalDateTime time;
    double temp;
    double hum;
    double press;
    DataProvider dataProvider;
    private
    GetSensData gsd;


    @Override
    public void run() {
        dataProvider = DataProvider.getInst();


        while (true) {

            if (dataProvider.isSensorUse()){
                gsd = GetSensData.getInst();
                time = LocalDateTime.now();
                temp = gsd.getTemp();
                hum = gsd.getHum();
                press = gsd.getPressure();
            } else {
                time = LocalDateTime.now();
                temp = Math.random() * 10;
                hum = Math.random() * 100;
                press = Math.random() * 750;
            }

            dataProvider.updateInfo(time,temp,hum,press);

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
