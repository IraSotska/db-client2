package com.sotska.entity;

public class ReportData {

    private String[] columnNames;
    private Object[][] data;
    private int updatedRows;
    private boolean isUpdate;

    public ReportData(String[] columnNames, Object[][] data, boolean isUpdate) {
        this.columnNames = columnNames;
        this.data = data;
        this.isUpdate = isUpdate;
    }

    public ReportData(int updatedRows, boolean isUpdate) {
        this.updatedRows = updatedRows;
        this.isUpdate = isUpdate;
    }

    public int getUpdatedRows() {
        return updatedRows;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public Object[][] getData() {
        return data;
    }

    public boolean isUpdate() {
        return isUpdate;
    }
}
