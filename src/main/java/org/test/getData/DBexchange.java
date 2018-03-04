package org.test.getData;

import java.sql.*;
import java.util.*;

public class DBexchange {
    private final String SQLITE_DB = "jdbc:sqlite:prod.db";
    private final String DRIVER_NAME = "org.sqlite.JDBC";
    private final String NAME_TABLE = "data";
    private final String SQL_CREATE_TABLE =
            "DROP TABLE IF EXISTS " + NAME_TABLE + ";" +
                    "CREATE TABLE " + NAME_TABLE +
                    "(login  CHAR(6) PRIMARY KEY NOT NULL," +
                    " passwd CHAR(6) NOT NULL);";
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

    void update(String login, String passwd) { // update passwd by login
        try {
            stmt.executeUpdate("UPDATE " + NAME_TABLE +
                    " set PASSWD='" + passwd +
                    "' where LOGIN='" + login + "';");
        } catch (Exception e) {
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
}

