package com.sotska.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {

    @Test
    void loadConfig() {
        var config = ConfigLoader.loadConfig("test.properties");
        assertEquals("pwd", config.getPassword());
        assertEquals("ira", config.getUser());
        assertEquals("C:/reports/", config.getReportsPath());
        assertEquals("jdbc:postgresql://localhost:5432/", config.getUrl());
    }
}