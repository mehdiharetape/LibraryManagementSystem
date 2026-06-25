package com.library.infrastructure.persistance;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;

    private ConfigManager(){
        properties = new Properties();
        //read config file
        try(InputStream in = new FileInputStream("db.properties")){
            properties.load(in);
            System.out.println("---Config File loaded Successfully---");
        }
        catch (IOException e){
            System.out.println("Error to read config file");
            e.printStackTrace();
        }
    }

    public static ConfigManager getInstance(){
        if(instance == null)
            instance = new ConfigManager();
        return instance;
    }

    //get values by key
    public String getProperty(String key){
        return properties.getProperty(key);
    }
}
