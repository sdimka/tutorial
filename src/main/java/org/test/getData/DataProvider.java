package org.test.getData;

public class DataProvider {
    private static DataProvider instance = new DataProvider();
    private double currentTemp;
    private double currentHum;
    private double currentPress;
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
    }

    public static DataProvider getInst() {
        return instance;
    }

    public void updateTemp() {

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
