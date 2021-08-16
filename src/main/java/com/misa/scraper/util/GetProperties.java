package com.misa.scraper.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetProperties {
    private static final Logger logger = Logger.getLogger(GetProperties.class.getName());
    private static Properties properties;

    public GetProperties() {

    }
    public static void loadFile() throws IOException {
        try (InputStream input = GetProperties.class
                .getClassLoader().getResourceAsStream("properties/app.properties")) {

            getProperties(input);
        } catch (NullPointerException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
        }
    }

    private static void getProperties(InputStream in) throws IOException {
        properties = new Properties();
        properties.load(in);
    }

    public static String get(String name) {
        if(properties == null)
            try {
                loadFile();
            } catch (IOException e) {
                logger.log(Level.WARNING, e.getMessage());
            }

        return properties.getProperty(name);
    }
}
