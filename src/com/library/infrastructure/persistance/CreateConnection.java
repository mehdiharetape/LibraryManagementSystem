package com.library.infrastructure.persistance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateConnection {
    private static CreateConnection instance;
    private Connection connection;

    private CreateConnection(){
        //getting values from ConfigManager
        ConfigManager config = ConfigManager.getInstance();
        String url = config.getProperty("db.url");
        String username = config.getProperty("db.user");
        String password = config.getProperty("db.password");

        try {
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("----- Database connected -----");
        }
        catch (SQLException e){
            System.out.println("!!! Database connection error !!!");
        }
    }

    public static CreateConnection getInstance(){
        if(instance == null)
            instance = new CreateConnection();
        return instance;
    }

    public Connection getConnection(){
        return connection;
    }

    public void closeConnection(){
        try {
            connection.close();
            System.out.println("++++++++Connection Closed+++++++");
        }
        catch (SQLException e){
            System.out.println("DB close Error : " + e.getMessage());
        }
    }
}
