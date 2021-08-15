package com.ewus.ba.energielenkerEneffcoService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class Config {
    public static Properties readDbProperties() {
        return readProperties("src/main/resources/dbConfig.properties",
                new String[] { "dbHost", "dbName", "dbUsername", "dbPassword" });
    }

    public static String readProperty(String filename, String key) {
        Properties props;
        try {
            props = readPropertiesFile(filename);
            return props.getProperty(key);
        } catch (IOException e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return null;
    }

    public static Properties readProperties(String filename, String[] keys) {
        Properties allProps;
        Properties props = new Properties();
        try {
            allProps = readPropertiesFile(filename);
            for (String key : keys) {
                props.setProperty(key, allProps.getProperty(key));
            }
        } catch (IOException e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return props;
    }

    public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream(fileName);
            prop = new Properties();
            prop.load(fis);
        } catch (FileNotFoundException fnfe) {
            Utils.LOGGER.log(Level.WARNING, fnfe.getMessage(), fnfe);
        } catch (IOException ioe) {
            Utils.LOGGER.log(Level.WARNING, ioe.getMessage(), ioe);

        } finally {
            fis.close();
        }
        return prop;
    }
}
