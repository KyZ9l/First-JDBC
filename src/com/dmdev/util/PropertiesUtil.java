package com.dmdev.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    
    private static final Properties PROPERTIES = new Properties();


    static {
        lodeProperties();
    }

    private static void lodeProperties() {
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
                    PROPERTIES.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String get(String key)
    {
        return PROPERTIES.getProperty(key);
    }


    private PropertiesUtil() {
    }
}
