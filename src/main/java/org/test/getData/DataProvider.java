package org.test.getData;

import java.time.LocalDateTime;
import java.time.Period;

public class DataProvider {
    private static DataProvider instance = new DataProvider();
    private volatile double currentTemp;
    private volatile double currentHum;
    private volatile double currentPress;
    private volatile boolean sensorUse;
    private final String SQL_CREATE_TABLE =
            "DROP TABLE IF EXISTS " + "data" + ";" +
                    "CREATE TABLE " + "data" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " time DATETIME, " +
                    " temp DOUBLE," +
                    " hum DOUBLE," +
                    " press DOUBLE);";
    DBexchange db;

    private DataProvider(){
        // GetSensData gsd = GetSensData.getInst();

        db = new DBexchange();
    //    db.createTable(SQL_CREATE_TABLE);
        currentTemp = 15;
        currentHum = 60;
        currentPress = 740;
        sensorUse = false;
        backGroundTask BGT = new backGroundTask();
        new Thread(BGT).start();
    }

    public static DataProvider getInst() {
        return instance;
    }

    public void updateInfo(LocalDateTime time, double temp, double hum, double press) {
        currentTemp = temp;
        currentHum = hum;
        currentPress = press;

        db.update(time, temp, hum, press);

    }

    public double getTemp() {
        return currentTemp;
    }

    public double getPressure() {
        return currentPress;
    }

    public double getHum() {
        return currentHum;
    }

    public boolean isSensorUse() {
        return sensorUse;
    }

    public void enableSensor() {
//        this.sensorUse = true;
    }

    public void detTempData(){
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime begin = end.minus(Period.ofDays(3));
        db.getListDataByPeriod(begin, end, "temp");
    }
}
