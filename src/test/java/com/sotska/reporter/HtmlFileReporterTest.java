package com.sotska.reporter;

import com.sotska.entity.ReportData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class HtmlFileReporterTest {

    private String path = "src/test/resources/test-reports/";
    private static final String EXPECTED_RESPONSE = """
            <table>
            \t<tr>
            \t\t<th>column1</th>
            \t\t<th>column2</th>
            \t\t<th>column3</th>
            \t</tr>
            \t<tr>
            \t\t<td>data1</td>
            \t\t<td>data2</td>
            \t\t<td>data3</td>
            \t</tr>
            \t<tr>
            \t\t<td>data4</td>
            \t\t<td>data5</td>
            \t\t<td>data6</td>
            \t</tr>
            </table>
            """;
    private static final String[] COLUMN_NAMES = new String[]{"column1", "column2", "column3"};
    private static final String[][] TABLE_DATA = new String[][]{{"data1", "data2", "data3"}, {"data4", "data5", "data6"}};

    @AfterEach
    void clearFiles() {
        var files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    @DisplayName("Should Create Report By Path")
    @Test
    void shouldCreateReportByPath() throws IOException {
        var reportData = new ReportData(COLUMN_NAMES, TABLE_DATA, false);

        HtmlFileReporter.createReport(reportData, path);

        var reportFromFile = Files.readString((new File(path).listFiles())[0].toPath());

        assertEquals(EXPECTED_RESPONSE, reportFromFile);
    }

    @DisplayName("Should Build Report")
    @Test
    void shouldBuildReport() {
        var reportData = new ReportData(COLUMN_NAMES, TABLE_DATA, false);
        var result = HtmlFileReporter.buildReport(reportData);

        assertEquals(EXPECTED_RESPONSE, result);
    }

    @DisplayName("Should Save Report To File")
    @Test
    void shouldSaveReportToFile() throws IOException {
        var data = "some text";
        HtmlFileReporter.saveReportToFile(data, path);

        assertTrue(new File(path).exists());
        assertEquals(data, Files.readString(new File(path).listFiles()[0].toPath()));
    }
}