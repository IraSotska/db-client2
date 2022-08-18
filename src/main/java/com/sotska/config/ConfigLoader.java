package com.sotska.config;

import java.io.IOException;
import java.util.Properties;

import static java.util.Objects.isNull;

public class ConfigLoader {

    private final Properties properties;
    private final Properties systemProperties;

    public ConfigLoader(String path) {
        properties = new Properties();
        systemProperties = System.getProperties();

        try (var inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Can't load configuration properties", e);
        }
    }

    public String getProperty(String key) {
        return isNull(systemProperties.getProperty(key)) ? properties.getProperty(key) : systemProperties.getProperty(key);
    }
}
