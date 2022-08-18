package com.sotska;

import com.sotska.config.ConfigLoader;
import com.sotska.db.ConnectionFactory;
import com.sotska.db.QueryExecutor;
import com.sotska.reporter.OutputReporter;
import com.sotska.reporter.HtmlFileReporter;
import com.sotska.reporter.OutputReporter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        start();
    }

    private static void start() {

        var configLoader = new ConfigLoader("application.properties");
        var port = Integer.parseInt(configLoader.getProperty("port"));
        startWebServer(port, configLoader.getProperty("reportsPath"));

        try (var scanner = new Scanner(System.in);
             var connectionFactory = new ConnectionFactory(configLoader.getProperty("url"),
                     configLoader.getProperty("user"), configLoader.getProperty("password"))) {

            var queryExecutor = new QueryExecutor(connectionFactory.getConnection());

            while (true) {
                var query = scanner.nextLine();
                var reportData = queryExecutor.execute(query);

                HtmlFileReporter.createReport(reportData, configLoader.getProperty("reportsPath"), port);
                OutputReporter.printReport(reportData, System.out);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void startWebServer(int port, String webAppPath) {
        var webServerThread = new Thread(() -> {
            var httpServer = new HttpServer();
            httpServer.start(port, webAppPath);
        });
        webServerThread.start();
    }
}
