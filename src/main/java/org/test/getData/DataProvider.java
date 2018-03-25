package org.test.getData;

import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.data.provider.ListDataProvider;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private static DataProvider instance = new DataProvider();
    private volatile double currentTemp;
    private volatile double currentHum;
    private volatile double currentPress;
    private volatile boolean sensorUse;
    private final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + "data" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " time DATETIME, " +
                    " temp DOUBLE," +
                    " hum DOUBLE," +
                    " press DOUBLE);";
    DBexchange db;

    private DataProvider(){
        // GetSensData gsd = GetSensData.getInst();

        db = new DBexchange();
        db.createTable(SQL_CREATE_TABLE);  //
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
        this.sensorUse = true;
    }

    public ListSeries get24HData(String dataType){

        ListSeries ls = new ListSeries();
        List<SensorsSet> lss;
        List<SensorsSet> completedLss = new ArrayList<>();
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime begin = end.minus(Period.ofDays(1));

        lss = db.getListDataByPeriod(begin, end, dataType);

        int hoursCount = 1;
        int count = 0;
        double tempAccum = 0;
        begin = lss.get(0).getTime();
//        System.out.println(begin);
//        System.out.println(begin.plusHours(hoursCount));
//        lss.forEach(a -> System.out.println(a.getTime() + " " + a.getTemp()));

        for (SensorsSet s : lss){
            if(s.getTime().isBefore(begin.plusHours(hoursCount))){
                tempAccum += s.getTemp();
                count ++;
            } else {
                completedLss.add(new SensorsSet(begin, tempAccum/count));
//                System.out.println(begin + " " + s.getTemp() + " " + tempAccum + " " + count);
                tempAccum = s.getTemp();
                count = 1;
                begin = s.getTime();
            }

        }
        completedLss.forEach(a-> System.out.println(a.getTemp()));
        completedLss.forEach(a->ls.addData(a.getTemp()));
//        for (int i = 0; i < 24; i++){
//            begin = LocalDateTime.now();
//            System.out.println(begin.plusHours(i).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
//        }
        return ls;
    }

    public List<SensorsSet> getRowData(int recordsNumber){
       List<SensorsSet> sensorSetList = new ArrayList<>();


       return sensorSetList;
    }
}
