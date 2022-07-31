package com.sotska.reporter;

import com.sotska.entity.ReportData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OutputReporterTest {

    @DisplayName("Should Create Report For Select")
    @Test
    void shouldCreateReportForSelect() {
        var columnNames = new String[]{"column1", "column2", "column3"};
        var tableData = new String[][]{{"data1", "data2", "data3"}, {"data4", "data5", "data6"}};

        var expectedResult = " ___________________________\r\n" +
                " | column1| column2| column3|\r\n" +
                " |==========================|\r\n" +
                " | data1  | data2  | data3  |\r\n" +
                " | data4  | data5  | data6  |\n";

        var outputStream = new ByteArrayOutputStream();
        var reportData = new ReportData(columnNames, tableData, false);
        OutputReporter.printReport(reportData, outputStream);

        assertEquals(expectedResult, outputStream.toString());
    }

    @DisplayName("Should Create Report For Update")
    @Test
    void shouldCreateReportForUpdate() {
        var expectedResult = "Count of updated rows: 2";

        var outputStream = new ByteArrayOutputStream();
        var reportData = new ReportData(2, true);
        OutputReporter.printReport(reportData, outputStream);

        assertEquals(expectedResult, outputStream.toString());
    }
}