package org.test.getData;

import java.time.LocalDateTime;

public class DataProvider {
    private static DataProvider instance = new DataProvider();
    private volatile double currentTemp;
    private volatile double currentHum;
    private volatile double currentPress;
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
        db.createTable(SQL_CREATE_TABLE);
        currentTemp = 15;
        currentHum = 60;
        currentPress = 740;
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
}
