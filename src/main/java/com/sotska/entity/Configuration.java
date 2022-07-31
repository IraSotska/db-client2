package com.sotska.entity;

public class Configuration {

    private String reportsPath;
    private String url;
    private String user;

    public Configuration(String reportsPath, String url, String user, String password) {
        this.reportsPath = reportsPath;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private String password;

    public String getReportsPath() {
        return reportsPath;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
