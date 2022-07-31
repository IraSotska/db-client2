package com.sotska.config;

import com.sotska.entity.Configuration;

import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

    private static final String REPORTS_PATH = "reportsPath";
    private static final String URL = "url";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    public static Configuration loadConfig(String path) {

        var property = new Properties();
        try (var inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(path)) {
            property.load(inputStream);

            var systemProperties = System.getProperties();

            return new Configuration(
                    !systemProperties.containsKey(REPORTS_PATH) ?
                            property.getProperty(REPORTS_PATH) :
                            systemProperties.getProperty(REPORTS_PATH),
                    !systemProperties.containsKey(URL) ?
                            property.getProperty(URL) :
                            systemProperties.getProperty(URL),
                    !systemProperties.containsKey(USER) ?
                            property.getProperty(USER) :
                            systemProperties.getProperty(USER),
                    !systemProperties.containsKey(PASSWORD) ?
                            property.getProperty(PASSWORD) :
                            systemProperties.getProperty(PASSWORD));

        } catch (IOException e) {
            throw new RuntimeException("Can't load configuration properties", e);
        }
    }
}
