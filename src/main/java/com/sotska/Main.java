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
        var configLoader = new ConfigLoader("application.properties");
        var port = Integer.parseInt(configLoader.getProperty("web.server.port"));
        startWebServer(port, configLoader.getProperty("html.reportsPath"));

        try (var scanner = new Scanner(System.in);
             var connectionFactory = new ConnectionFactory(configLoader.getProperty("jdbc.url"),
                     configLoader.getProperty("jdbc.user"), configLoader.getProperty("jdbc.password"))) {

            var queryExecutor = new QueryExecutor(connectionFactory.getConnection());

            while (true) {
                var query = scanner.nextLine();
                var reportData = queryExecutor.execute(query);

                HtmlFileReporter.createReport(reportData, configLoader.getProperty("html.reportsPath"), port);
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
