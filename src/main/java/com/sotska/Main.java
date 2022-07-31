package com.sotska;

import com.sotska.config.ConfigLoader;
import com.sotska.db.ConnectionFactory;
import com.sotska.db.QueryExecutor;
import com.sotska.reporter.OutputReporter;
import com.sotska.reporter.HtmlFileReporter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var config = ConfigLoader.loadConfig("application.properties");
        try (var scanner = new Scanner(System.in);
             var connectionFactory = new ConnectionFactory(config.getUrl(), config.getUser(), config.getPassword())) {

            var queryExecutor = new QueryExecutor(connectionFactory.getConnection());

            while (true) {
                var query = scanner.nextLine();
                var reportData = queryExecutor.execute(query);

                OutputReporter.printReport(reportData, System.out);
                HtmlFileReporter.createReport(reportData, config.getReportsPath());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
