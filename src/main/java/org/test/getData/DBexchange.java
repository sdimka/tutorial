package org.test.getData;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

public class DBexchange {
    private final String SQLITE_DB = "jdbc:sqlite:prod.db";
    private final String DRIVER_NAME = "org.sqlite.JDBC";
    private final String NAME_TABLE = "data";
    private final String SQL_CREATE_TABLE =
            "DROP TABLE IF EXISTS " + "data" + ";" +
                    "CREATE TABLE " + "data" +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " time DATETIME, " +
                    " temp DOUBLE," +
                    " temp2 DOUBLE," +
                    " hum DOUBLE," +
                    " press DOUBLE);";
    private final String SQL_SELECT = "SELECT * FROM " + NAME_TABLE + ";";
    private final String DB_CREATED = "Darabase created.";
    private final String RECORD_ADDED = "Record added.";
    private final String RECORD_DELETED = "Record deleted.";
    private final String RECORD_UPDATED = "Record updated.";
    private final String UNKNOWN_COMMAND = "Unknown command, use [-c,-a,-r,-u,-d] only.";
    private final String LOGIN_COL = "login";

    Connection connect;
    Statement stmt;
    ResultSet rs;
    String sql;

    public static void main(String[] args) {
        DBexchange db = new DBexchange();
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime begin = end.minus(Period.ofDays(3));
        db.getListDataByPeriod(begin, end, "temp");
    }

    DBexchange() {
        openDBFile(SQLITE_DB);
    }

    private DBexchange openDBFile(String dbName) { // open/create database
        try {
            Class.forName(DRIVER_NAME);
            connect = DriverManager.getConnection(dbName);
            stmt = connect.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    void createTable(String sqlCreateTable) { // create table
        try {
            stmt.executeUpdate(sqlCreateTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void add(String login, String passwd) { // add record

        try {
            stmt.executeUpdate("INSERT INTO " + NAME_TABLE +
                    " (login, passwd) " +
                    "VALUES ('" + login + "', '" + passwd + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void update(LocalDateTime time, double temp, double temp2, double hum, double press) { // update passwd by login

        try {
            PreparedStatement ps = connect.prepareStatement("INSERT INTO " +
                    NAME_TABLE + "(time, temp, temp2, hum, press) " +
                    "VALUES(?, ?, ?, ?, ?);");

            ps.setTimestamp(1, Timestamp.valueOf(time));
            ps.setDouble(2, temp);
            ps.setDouble(3, temp);
            ps.setDouble(4, hum);
            ps.setDouble(5, press);
            ps.addBatch();
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    void delete(String login) { // delete record by login
        try {
            stmt.executeUpdate("DELETE from " + NAME_TABLE +
                    " where LOGIN='" + login + "';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void list() { // show all records
        try {
            System.out.println("LOGIN\t\tPASSWD");
            rs = stmt.executeQuery(SQL_SELECT);
            while (rs.next())
                System.out.println(
                        rs.getString(LOGIN_COL) + "\t\t" +
                                rs.getString("PASSWD_COL"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SensorsSet> getListDataByPeriod(LocalDateTime begin, LocalDateTime end, String param){

        List<SensorsSet> res = new ArrayList<>();
        String sql = "SELECT *" + " FROM " + NAME_TABLE + " WHERE time > " +
                begin.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + //param +
                 " AND time < " +
                end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                res.add(new SensorsSet(LocalDateTime.ofInstant(Instant.ofEpochMilli(rs.getLong("time")),
                        TimeZone.getDefault().toZoneId()),rs.getDouble(param)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    return res;
    }
}

