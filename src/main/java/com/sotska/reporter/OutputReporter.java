package com.sotska.reporter;

import com.sotska.entity.ReportData;
import dnl.utils.text.table.TextTable;

import java.io.OutputStream;
import java.io.PrintStream;

public class OutputReporter {

    public static void printReport(ReportData reportData, OutputStream outputStream) {

        var printStream = new PrintStream(outputStream);
        if (reportData.isUpdate()) {
            printStream.print("Count of updated rows: " + reportData.getUpdatedRows());
        } else {
            var textTable = new TextTable((reportData).getColumnNames(), reportData.getData());
            textTable.printTable(new PrintStream(outputStream), 1);
        }
    }
}
