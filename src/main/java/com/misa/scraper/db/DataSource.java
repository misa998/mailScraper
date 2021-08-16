package com.misa.scraper.db;

import com.misa.scraper.util.GetProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSource {
    private static final Logger logger = Logger.getLogger(DataSource.class.getName());

    private static DataSource instance = new DataSource();
    private DataSource(){}
    public static DataSource getInstance(){
        return instance;
    }

    private Connection connection;

    public Connection openConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    getURL(),
                    GetProperties.get("db.username"),
                    GetProperties.get("db.password"));
            return connection;
        } catch (SQLException | ClassNotFoundException e){
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
            return null;
        }
    }

    public void closeConnection(){
        try{
            connection.close();
        } catch (SQLException | NullPointerException e){
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public String getURL() {
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://");
        sb.append(GetProperties.get("db.ip"));
        sb.append(":");
        sb.append(GetProperties.get("db.port"));
        sb.append("/");
        sb.append(GetProperties.get("db.schema"));
        return sb.toString();
    }
}

