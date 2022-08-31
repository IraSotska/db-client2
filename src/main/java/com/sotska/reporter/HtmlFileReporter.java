package com.sotska.reporter;

import com.sotska.entity.ReportData;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HtmlFileReporter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("_yyyy-MM-dd_HH-mm-ss");

    public static void createReport(ReportData reportData, String reportPath, int port) {
        if (reportData.isUpdate()) {
            return;
        }
        var report = buildReport(reportData);
        reportData.setReportPath("http://localhost:" + port + "/" + saveReportToFile(report, reportPath));
    }

    static String buildReport(ReportData reportData) {
        int columnCount = reportData.getColumnNames().length;
        var stringBuilder = new StringBuilder();
        stringBuilder.append("<table>").append("\t<tr>");

        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            stringBuilder.append("<th>").append(reportData.getColumnNames()[columnIndex]).append("</th>");
        }
        stringBuilder.append("</tr>");

        for (int rowIndex = 0; rowIndex < reportData.getData().length; rowIndex++) {
            stringBuilder.append("<tr>");
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                stringBuilder.append("<td>").append(reportData.getData()[rowIndex][columnIndex]).append("</td>");
            }
            stringBuilder.append("</tr>");
        }
        stringBuilder.append("</table>");
        return StringEscapeUtils.escapeHtml(stringBuilder.toString());
    }

    static String saveReportToFile(String report, String path) {
        var reportFileName = "report" + getFileReporterPrefix() + ".html";
        var reportFile = new File(path, reportFileName);
        reportFile.getParentFile().mkdir();

        try (var bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportFile)))) {
            bufferedWriter.write(report);
            return reportFileName;
        } catch (IOException e) {
            throw new RuntimeException("Can't save report to file by path: " + path, e);
        }
    }

    private static String getFileReporterPrefix() {
        return DATE_TIME_FORMATTER.format(LocalDateTime.now());
    }
}
