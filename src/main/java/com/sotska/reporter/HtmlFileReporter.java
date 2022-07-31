package com.sotska.reporter;

import com.sotska.entity.ReportData;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HtmlFileReporter {

    public static void createReport(ReportData reportData, String reportPath) {
        if (reportData.isUpdate()) {
            return;
        }
        var report = buildReport(reportData);
        saveReportToFile(report, reportPath);
    }

    static String buildReport(ReportData reportData) {
        int columnCount = reportData.getColumnNames().length;
        var stringBuilder = new StringBuilder();
        stringBuilder.append("<table>\n").append("\t<tr>\n");

        for (int i = 0; i < columnCount; i++) {
            stringBuilder.append("\t\t<th>").append(reportData.getColumnNames()[i]).append("</th>\n");
        }
        stringBuilder.append("\t</tr>\n");

        for (int i = 0; i < reportData.getData().length; i++) {
            stringBuilder.append("\t<tr>\n");
            for (int j = 0; j < columnCount; j++) {
                stringBuilder.append("\t\t<td>").append(reportData.getData()[i][j]).append("</td>\n");
            }
            stringBuilder.append("\t</tr>\n");
        }
        stringBuilder.append("</table>\n");
        return stringBuilder.toString();
    }

    static void saveReportToFile(String report, String path) {
        var reportFile = new File(path, "report" + getFileReporterPrefix() + ".html");
        reportFile.getParentFile().mkdir();

        try (var bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportFile)))) {
            bufferedWriter.write(report);
        } catch (IOException e) {
            throw new RuntimeException("Can't save report to file by path: " + path, e);
        }
    }

    private static String getFileReporterPrefix() {
        var dateTimeFormatter = DateTimeFormatter.ofPattern("_yyyy-MM-dd-HH__mm-ss");
        return dateTimeFormatter.format(LocalDateTime.now());
    }
}
